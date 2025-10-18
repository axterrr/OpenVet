package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PendingOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.VaccinationRecordEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.VaccinationRecordRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.PetMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PendingOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.VaccinationRecordRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.PetValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.PetPermissionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService extends BaseService<PetEntity, PetRequest, Long> {

    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PendingOwnerRepository pendingOwnerRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final UserRepository userRepository;

    private final PetPermissionValidator petPermissionValidator;

    public PetService(
        PetRepository petRepository,
        PetOwnerRepository petOwnerRepository,
        PendingOwnerRepository pendingOwnerRepository,
        VaccinationRecordRepository vaccinationRecordRepository,
        VeterinarianRepository veterinarianRepository,
        UserRepository userRepository,
        PetPermissionValidator petPermissionValidator,
        PetMerger merger,
        PetValidator validator
    ) {
        super(petRepository, merger, validator, petPermissionValidator);
        this.petRepository = petRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.pendingOwnerRepository = pendingOwnerRepository;
        this.vaccinationRecordRepository = vaccinationRecordRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.userRepository = userRepository;
        this.petPermissionValidator = petPermissionValidator;
    }

    @Override
    protected PetEntity newEntity() {
        return new PetEntity();
    }

    public List<PetEntity> getByOwnerId(Long ownerId) {
        List<PetEntity> pets = petRepository.findByOwnerId(ownerId);
        List<PetEntity> allowed = new ArrayList<>();
        for (PetEntity pet : pets) {
            try {
                petPermissionValidator.validateForGet(pet);
                allowed.add(pet);
            }
            catch (Exception ignored) {}
        }
        return allowed;
    }

    public List<PetEntity> findUnassignedPets() {
        List<PetEntity> pets = petRepository.findByOwnerIsNull();
        List<PetEntity> allowed = new ArrayList<>();
        for (PetEntity pet : pets) {
            try {
                petPermissionValidator.validateForGet(pet);
                allowed.add(pet);
            }
            catch (Exception ignored) {}
        }
        return allowed;
    }

    public PetEntity findByMicrochipNumber(String microchipNumber) {
        PetEntity pet = petRepository.findByMicrochipNumber(microchipNumber)
            .orElseThrow(() -> new NotFoundException("Pet not found"));
        petPermissionValidator.validateForGet(pet);
        return pet;
    }

    public List<PetEntity> findByOwnerOrPendingPhone(String phone) {
        List<PetEntity> pets = petRepository.findByOwnerPhoneNumberOrPendingOwnerPhoneNumber(phone, phone);
        List<PetEntity> allowed = new ArrayList<>();
        for (PetEntity pet : pets) {
            try { petPermissionValidator.validateForGet(pet); allowed.add(pet); } catch (Exception ignored) {}
        }
        return allowed;
    }

    public PetEntity setOwner(Long petId, Long ownerId) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForOwnershipChange(pet);
        PetOwnerEntity owner = petOwnerRepository.findById(ownerId)
            .orElseThrow(() -> new NotFoundException("Owner not found"));
        pet.setOwner(owner);
        pet.setPendingOwner(null);
        return petRepository.saveAndFlush(pet);
    }

    public PetEntity clearOwner(Long petId) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForOwnershipChange(pet);
        pet.setOwner(null);
        return petRepository.saveAndFlush(pet);
    }

    public PetEntity setPendingOwnerByPhone(Long petId, String phone) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForSetPendingOwner(pet);
        PendingOwnerEntity pending = pendingOwnerRepository.findByPhoneNumber(phone)
            .orElseThrow(() -> new NotFoundException("Pending owner not found"));
        if (pending == null) {
            pending = PendingOwnerEntity.builder().phoneNumber(phone).build();
            pending = pendingOwnerRepository.saveAndFlush(pending);
        }
        pet.setPendingOwner(pending);
        pet.setOwner(null);
        return petRepository.saveAndFlush(pet);
    }

    public PetEntity clearPendingOwner(Long petId) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForOwnershipChange(pet);
        pet.setPendingOwner(null);
        return petRepository.saveAndFlush(pet);
    }

    public List<PetEntity> attachPendingPetsToOwnerByPhone(String phone, Long ownerId) {
        PetOwnerEntity owner = petOwnerRepository.findById(ownerId)
            .orElseThrow(() -> new NotFoundException("Owner not found"));
        List<PetEntity> pets = petRepository.findByPendingOwnerPhoneNumber(phone);
        List<PetEntity> updated = new ArrayList<>();
        for (PetEntity pet : pets) {
            petPermissionValidator.validateForOwnershipChange(pet);
            pet.setOwner(owner);
            pet.setPendingOwner(null);
            updated.add(petRepository.saveAndFlush(pet));
        }

        Optional<PendingOwnerEntity> pending = pendingOwnerRepository.findByPhoneNumber(phone);
        if (pending.isPresent() && petRepository.findByPendingOwnerPhoneNumber(phone).isEmpty()) {
            pendingOwnerRepository.deleteById(pending.get().getId());
        }
        return updated;
    }

    public VaccinationRecordEntity addVaccinationRecord(Long petId, VaccinationRecordRequest request) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForAddVaccinationRecord(pet);

        VeterinarianEntity vet = getAuthenticatedVeterinarian();

        // Should not happen due to permission validator, but just in case :skull:
        if (vet == null) {
            throw new NotFoundException("Authenticated veterinarian not found");
        }

        VaccinationRecordEntity record = VaccinationRecordEntity.builder()
            .vaccineName(request.getVaccineName())
            .manufacturer(request.getManufacturer())
            .batchNumber(request.getBatchNumber())
            .notes(request.getNotes())
            .vaccinationDate(request.getVaccinationDate())
            .nextDueDate(request.getNextDueDate())
            .veterinarian(vet)
            .pet(pet)
            .build();
        return vaccinationRecordRepository.saveAndFlush(record);
    }

    public List<VaccinationRecordEntity> getVaccinationRecords(Long petId) {
        PetEntity pet = getById(petId);
        petPermissionValidator.validateForGet(pet);
        return vaccinationRecordRepository.findByPetId(petId);
    }

    private VeterinarianEntity getAuthenticatedVeterinarian() {
        String email = getAuthenticatedEmail();
        if (email == null) { return null; }
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) { return null; }
        return veterinarianRepository.findById(user.getId()).orElse(null);
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) { return null; }
        Object principal = authentication.getPrincipal();
        return (principal instanceof String) ? (String) principal : null;
    }
}

