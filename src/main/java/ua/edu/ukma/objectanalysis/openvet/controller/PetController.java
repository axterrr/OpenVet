package ua.edu.ukma.objectanalysis.openvet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.VaccinationRecordEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetResponse;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.VaccinationRecordRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.VaccinationRecordResponse;
import ua.edu.ukma.objectanalysis.openvet.service.PetService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;


    @GetMapping("")
    public ResponseEntity<List<PetResponse>> getPets() {
        return new ResponseEntity<>(petService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> create(@RequestBody PetRequest request) {
        petService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long id) {
        return new ResponseEntity<>(map(petService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody PetRequest request) {
        petService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        petService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Owner-based requests

    @GetMapping("owner/{ownerId}")
    public ResponseEntity<List<PetResponse>> getByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(petService.getByOwnerId(ownerId).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("microchip/{chip}")
    public ResponseEntity<List<PetResponse>> getByChip(@PathVariable("chip") String microchip) {
        return new ResponseEntity<>(petService.findByMicrochipNumber(microchip).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("phone/{phone}")
    public ResponseEntity<List<PetResponse>> getByPhone(@PathVariable String phone) {
        return new ResponseEntity<>(petService.findByOwnerOrPendingPhone(phone).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("unassigned")
    public ResponseEntity<List<PetResponse>> getUnassigned() {
        return new ResponseEntity<>(petService.findUnassignedPets().stream().map(this::map).toList(), HttpStatus.OK);
    }

    // Ownership flow

    @PostMapping("{id}/owner/{ownerId}")
    public ResponseEntity<HttpStatus> setOwner(@PathVariable Long id, @PathVariable Long ownerId) {
        petService.setOwner(id, ownerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}/owner")
    public ResponseEntity<HttpStatus> clearOwner(@PathVariable Long id) {
        petService.clearOwner(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Pending owner flow

    @PostMapping("{id}/pending-owner")
    public ResponseEntity<HttpStatus> setPendingOwner(@PathVariable Long id, @RequestParam String phone) {
        petService.setPendingOwnerByPhone(id, phone);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}/pending-owner")
    public ResponseEntity<HttpStatus> clearPendingOwner(@PathVariable Long id) {
        petService.clearPendingOwner(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
     * TODO: ideally, this is redundant, because services should automatically attach pending pets to owners
     *       when an owner is created.
     */
    @PostMapping("pending/attach")
    public ResponseEntity<List<PetResponse>> attachPendingToOwner(@RequestParam String phone, @RequestParam Long ownerId) {
        List<PetEntity> updated = petService.attachPendingPetsToOwnerByPhone(phone, ownerId);
        return new ResponseEntity<>(updated.stream().map(this::map).toList(), HttpStatus.OK);
    }

    // Vaccination flow

    @GetMapping("{id}/vaccinations")
    public ResponseEntity<List<VaccinationRecordResponse>> getVaccinationRecords(@PathVariable Long id) {
        List<VaccinationRecordEntity> records = petService.getVaccinationRecords(id);
        return new ResponseEntity<>(records.stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("{id}/vaccinations")
    public ResponseEntity<HttpStatus> addVaccination(@PathVariable Long id, @RequestBody VaccinationRecordRequest request) {
        petService.addVaccinationRecord(id, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // ---- Mappers ----

    private PetResponse map(PetEntity e) {
        if (e == null) { return null; }
        return PetResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .species(e.getSpecies())
            .breed(e.getBreed())
            .birthDate(e.getBirthDate())
            .sex(e.getSex())
            .color(e.getColor())
            .neutered(e.isNeutered())
            .microchipNumber(e.getMicrochipNumber())
            .ownerId(e.getOwner() != null ? e.getOwner().getId() : null)
            .ownerPhone(e.getOwner() != null ? e.getOwner().getPhoneNumber() : null)
            .pendingOwnerPhone(e.getPendingOwner() != null ? e.getPendingOwner().getPhoneNumber() : null)
            .build();
    }

    private VaccinationRecordResponse map(VaccinationRecordEntity r) {
        if (r == null) { return null; }
        return VaccinationRecordResponse.builder()
            .id(r.getId())
            .vaccineName(r.getVaccineName())
            .manufacturer(r.getManufacturer())
            .batchNumber(r.getBatchNumber())
            .notes(r.getNotes())
            .vaccinationDate(r.getVaccinationDate())
            .nextDueDate(r.getNextDueDate())
            .veterinarianId(r.getVeterinarian() != null ? r.getVeterinarian().getId() : null)
            .build();
    }
}
