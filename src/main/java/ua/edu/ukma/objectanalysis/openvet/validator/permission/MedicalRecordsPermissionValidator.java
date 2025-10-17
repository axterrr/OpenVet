package ua.edu.ukma.objectanalysis.openvet.validator.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.VeterinarianRepository;

@Component
@RequiredArgsConstructor
public class MedicalRecordsPermissionValidator extends BasePermissionValidator<MedicalRecordsEntity, MedicalRecordsRequest> {

    private final AppointmentRepository appointmentRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetRepository petRepository;

    @Override
    public void validateForGetAll() {
        requireUserRole(UserRole.ADMIN);
    }

    @Override
    public void validateForGet(MedicalRecordsEntity entity) {
        if (isAuthenticatedUserAdmin()) {
            return;
        }
        if (isAuthenticatedUserVeterinarian()) {
            requireUserEmail(entity.getAppointment().getTimeSlot().getVeterinarian().getEmail());
        }
        if (isAuthenticatedUserPetOwner()) {
            requireUserEmail(entity.getAppointment().getPet().getOwner().getEmail());
        }
    }

    @Override
    public void validateForCreate(MedicalRecordsRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new NotFoundException("Appointment not found: " + request.getAppointmentId()));
        requireUserEmail(appointment.getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForUpdate(MedicalRecordsEntity entity) {
        requireUserEmail(entity.getAppointment().getTimeSlot().getVeterinarian().getEmail());
    }

    @Override
    public void validateForDelete(MedicalRecordsEntity entity) {
        requireUserRole(UserRole.ADMIN);
    }

    public void validateForGetByVeterinarian(Long veterinarianId) {
        VeterinarianEntity veterinarian = veterinarianRepository.findById(veterinarianId)
            .orElseThrow(() -> new NotFoundException("Veterinarian not found"));
        if (isAuthenticatedUserAdmin()) { return; }
        requireUserEmail(veterinarian.getEmail());
    }

    public void validateForGetByPet(Long petId) {
        PetEntity pet = petRepository.findById(petId)
            .orElseThrow(() -> new NotFoundException("Pet not found"));
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) { return; }
        requireUserEmail(pet.getOwner().getEmail());
    }

    public void validateForGetByPetOwner(Long ownerId) {
        PetOwnerEntity owner = petOwnerRepository.findById(ownerId)
            .orElseThrow(() -> new NotFoundException("Owner not found"));
        if (isAuthenticatedUserAdmin() || isAuthenticatedUserVeterinarian()) { return; }
        requireUserEmail(owner.getEmail());
    }
}
