package ua.edu.ukma.objectanalysis.openvet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleResponse;
import ua.edu.ukma.objectanalysis.openvet.service.ScheduleService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("")
    public ResponseEntity<List<ScheduleResponse>> getSchedules() {
        return new ResponseEntity<>(scheduleService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody ScheduleRequest request) {
        scheduleService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(map(scheduleService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        scheduleService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        scheduleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("veterinarian/{id}")
    public ResponseEntity<List<ScheduleResponse>> getVeterinarianSchedule(@PathVariable Long id) {
        List<ScheduleEntity> list = scheduleService.getForVeterinarian(id);
        return new ResponseEntity<>(list.stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("veterinarian/{id}/weekly")
    public ResponseEntity<Map<DayOfWeek, List<ScheduleResponse>>> getVeterinarianScheduleMap(@PathVariable Long id) {
        Map<DayOfWeek, List<ScheduleEntity>> map = scheduleService.getForVeterinarianMap(id);
        Map<DayOfWeek, List<ScheduleResponse>> dto = map.entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().map(this::map).toList()
            ));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("day/{dayOfWeek}")
    public ResponseEntity<List<ScheduleResponse>> getByDay(@PathVariable DayOfWeek dayOfWeek) {
        return new ResponseEntity<>(scheduleService.findByDayOfWeek(dayOfWeek).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("veterinarian/{id}/day/{dayOfWeek}")
    public ResponseEntity<ScheduleResponse> getByVeterinarianAndDay(@PathVariable Long id, @PathVariable DayOfWeek dayOfWeek) {
        return new ResponseEntity<>(map(scheduleService.getByVeterinarianAndDay(id, dayOfWeek)), HttpStatus.OK);
    }

    private ScheduleResponse map(ScheduleEntity e) {
        if (e == null) { return null; }
        return ScheduleResponse.builder()
            .id(e.getId())
            .veterinarianId(e.getVeterinarian() != null ? e.getVeterinarian().getId() : null)
            .dayOfWeek(e.getDayOfWeek())
            .startTime(e.getStartTime())
            .endTime(e.getEndTime())
            .breakStartTime(e.getBreakStartTime())
            .breakEndTime(e.getBreakEndTime())
            .build();
    }
}
