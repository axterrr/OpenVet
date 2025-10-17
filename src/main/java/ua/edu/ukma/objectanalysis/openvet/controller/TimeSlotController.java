package ua.edu.ukma.objectanalysis.openvet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.TimeSlotEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.TimeSlotStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.TimeSlotResponse;
import ua.edu.ukma.objectanalysis.openvet.service.TimeSlotService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("")
    public ResponseEntity<List<TimeSlotResponse>> getAll() {
        return new ResponseEntity<>(timeSlotService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody TimeSlotRequest request) {
        timeSlotService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeSlotResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(map(timeSlotService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody TimeSlotRequest request) {
        timeSlotService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        timeSlotService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("veterinarian/{veterinarianId}/range")
    public ResponseEntity<List<TimeSlotResponse>> getForVeterinarianInRange(
            @PathVariable Long veterinarianId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            @RequestParam(required = false) TimeSlotStatus status
    ) {
        List<TimeSlotEntity> slots = timeSlotService.getForVeterinarianInRange(veterinarianId, from, to);
        if (status != null) {
            slots = slots.stream().filter(s -> s.getStatus() == status).collect(Collectors.toList());
        }
        return new ResponseEntity<>(slots.stream().map(this::map).toList(), HttpStatus.OK);
    }

    // Merge operations

    // Merge contiguous slots for a veterinarian in a range; returns number of merges performed
    @PostMapping("veterinarian/{veterinarianId}/merge-contiguous")
    public ResponseEntity<Integer> mergeContiguous(
            @PathVariable Long veterinarianId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        int merged = timeSlotService.mergeContiguous(veterinarianId, from, to);
        return new ResponseEntity<>(merged, HttpStatus.OK);
    }

    // Merge a provided ordered/unordered list of slots ids into one
    @PostMapping("merge/list")
    public ResponseEntity<HttpStatus> mergeList(@RequestBody List<Long> slotIds) {
        timeSlotService.mergeSlotsList(slotIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Appointment-based operations

    @PostMapping("{slotId}/appointment/{appointmentId}")
    public ResponseEntity<TimeSlotResponse> linkAppointment(@PathVariable Long slotId, @PathVariable Long appointmentId) {
        return new ResponseEntity<>(map(timeSlotService.linkAppointment(slotId, appointmentId)), HttpStatus.OK);
    }

    @DeleteMapping("{slotId}/appointment")
    public ResponseEntity<TimeSlotResponse> unlinkAppointment(@PathVariable Long slotId) {
        return new ResponseEntity<>(map(timeSlotService.unlinkAppointment(slotId)), HttpStatus.OK);
    }

    private TimeSlotResponse map(TimeSlotEntity e) {
        if (e == null) { return null; }
        return TimeSlotResponse.builder()
            .id(e.getId())
            .veterinarianId(e.getVeterinarian() != null ? e.getVeterinarian().getId() : null)
            .startTime(e.getStartTime())
            .endTime(e.getEndTime())
            .status(e.getStatus())
            .appointmentId(e.getAppointment() != null ? e.getAppointment().getId() : null)
            .build();
    }
}
