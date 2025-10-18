package ua.edu.ukma.objectanalysis.openvet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentResponse;
import ua.edu.ukma.objectanalysis.openvet.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("")
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return new ResponseEntity<>(appointmentService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody AppointmentRequest request) {
        appointmentService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AppointmentResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(map(appointmentService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        appointmentService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        appointmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ---- Business actions ----

    @PostMapping("{id}/cancel/owner")
    public ResponseEntity<AppointmentResponse> cancelByOwner(@PathVariable Long id) {
        return new ResponseEntity<>(map(appointmentService.cancelByOwner(id)), HttpStatus.OK);
    }

    @PostMapping("{id}/cancel/clinic")
    public ResponseEntity<AppointmentResponse> cancelByClinic(@PathVariable Long id) {
        return new ResponseEntity<>(map(appointmentService.cancelByClinic(id)), HttpStatus.OK);
    }

    @PostMapping("{id}/complete")
    public ResponseEntity<AppointmentResponse> markCompleted(@PathVariable Long id) {
        return new ResponseEntity<>(map(appointmentService.markCompleted(id)), HttpStatus.OK);
    }

    @PostMapping("{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(@PathVariable Long id, @RequestParam("timeSlotId") Long newTimeSlotId) {
        return new ResponseEntity<>(map(appointmentService.reschedule(id, newTimeSlotId)), HttpStatus.OK);
    }

    // ---- Queries ----

    @GetMapping("pet/{petId}")
    public ResponseEntity<List<AppointmentResponse>> getByPet(@PathVariable Long petId) {
        return new ResponseEntity<>(appointmentService.getByPetId(petId).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("owner/{ownerId}")
    public ResponseEntity<List<AppointmentResponse>> getByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(appointmentService.getByOwnerId(ownerId).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("veterinarian/{vetId}")
    public ResponseEntity<List<AppointmentResponse>> getByVeterinarian(@PathVariable("vetId") Long veterinarianId) {
        return new ResponseEntity<>(appointmentService.getByVeterinarianId(veterinarianId).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("veterinarian/{vetId}/range")
    public ResponseEntity<List<AppointmentResponse>> getByVeterinarianInRange(
            @PathVariable("vetId") Long veterinarianId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        return new ResponseEntity<>(appointmentService.getByVeterinarianInRange(veterinarianId, from, to).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<AppointmentResponse>> getByStatus(@PathVariable AppointmentStatus status) {
        return new ResponseEntity<>(appointmentService.getByStatus(status).stream().map(this::map).toList(), HttpStatus.OK);
    }

    // ---- Mapper ----

    private AppointmentResponse map(AppointmentEntity e) {
        if (e == null) { return null; }
        return AppointmentResponse.builder()
            .id(e.getId())
            .petId(e.getPet() != null ? e.getPet().getId() : null)
            .veterinarianId(e.getTimeSlot() != null && e.getTimeSlot().getVeterinarian() != null ? e.getTimeSlot().getVeterinarian().getId() : null)
            .timeSlotId(e.getTimeSlot() != null ? e.getTimeSlot().getId() : null)
            .timeSlotStartTime(e.getTimeSlot() != null ? e.getTimeSlot().getStartTime() : null)
            .timeSlotEndTime(e.getTimeSlot() != null ? e.getTimeSlot().getEndTime() : null)
            .reasonForVisit(e.getReasonForVisit())
            .status(e.getStatus())
            .notes(e.getNotes())
            .billingId(e.getBilling() != null ? e.getBilling().getId() : null)
            .build();
    }
}
