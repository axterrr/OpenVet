package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.ScheduleEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.ScheduleRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.ScheduleRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.ScheduleValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.SchedulePermissionValidator;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleService Tests")
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository repository;

    @Mock
    private SchedulePermissionValidator permissionValidator;

    @Mock
    private ScheduleValidator validator;

    @Mock
    private BaseMerger<ScheduleEntity, ScheduleRequest> merger;

    @InjectMocks
    private ScheduleService scheduleService;

    private ScheduleEntity testSchedule;
    private ScheduleRequest testRequest;

    @BeforeEach
    void setUp() {
        testSchedule = ScheduleEntity.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build();

        testRequest = ScheduleRequest.builder()
                .veterinarianId(1L)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build();
    }

    // ===== GET FOR VETERINARIAN Tests =====

    @Test
    @DisplayName("Should get schedules for veterinarian successfully")
    void testGetForVeterinarian_Success() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findByVeterinarianId(1L)).thenReturn(schedules);
        doNothing().when(permissionValidator).validateForMultiple(any());

        // Act
        List<ScheduleEntity> result = scheduleService.getForVeterinarian(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByVeterinarianId(1L);
        verify(permissionValidator).validateForMultiple(schedules);
    }

    @Test
    @DisplayName("Should return empty list when no schedules for veterinarian")
    void testGetForVeterinarian_EmptyList() {
        // Arrange
        when(repository.findByVeterinarianId(999L)).thenReturn(Collections.emptyList());
        doNothing().when(permissionValidator).validateForMultiple(any());

        // Act
        List<ScheduleEntity> result = scheduleService.getForVeterinarian(999L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET FOR VETERINARIAN MAP Tests =====

    @Test
    @DisplayName("Should get schedule map for veterinarian successfully")
    void testGetForVeterinarianMap_Success() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findByVeterinarianId(1L)).thenReturn(schedules);
        doNothing().when(permissionValidator).validateForMultiple(any());
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        Map<DayOfWeek, List<ScheduleEntity>> result = scheduleService.getForVeterinarianMap(1L);

        // Assert
        assertNotNull(result);
        assertEquals(7, result.size()); // All 7 days of week
        assertEquals(1, result.get(DayOfWeek.MONDAY).size());
        verify(repository).findByVeterinarianId(1L);
    }

    @Test
    @DisplayName("Should filter out schedules without permission in map")
    void testGetForVeterinarianMap_FilterUnauthorized() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findByVeterinarianId(1L)).thenReturn(schedules);
        doNothing().when(permissionValidator).validateForMultiple(any());
        doThrow(new RuntimeException("Forbidden")).when(permissionValidator).validateForGet(any());

        // Act
        Map<DayOfWeek, List<ScheduleEntity>> result = scheduleService.getForVeterinarianMap(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.get(DayOfWeek.MONDAY).size());
    }

    // ===== FIND BY DAY OF WEEK Tests =====

    @Test
    @DisplayName("Should find schedules by day of week successfully")
    void testFindByDayOfWeek_Success() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(schedules);
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        List<ScheduleEntity> result = scheduleService.findByDayOfWeek(DayOfWeek.MONDAY);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByDayOfWeek(DayOfWeek.MONDAY);
    }

    @Test
    @DisplayName("Should filter unauthorized schedules when finding by day")
    void testFindByDayOfWeek_FilterUnauthorized() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findByDayOfWeek(DayOfWeek.MONDAY)).thenReturn(schedules);
        doThrow(new RuntimeException("Forbidden")).when(permissionValidator).validateForGet(any());

        // Act
        List<ScheduleEntity> result = scheduleService.findByDayOfWeek(DayOfWeek.MONDAY);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ===== GET BY VETERINARIAN AND DAY Tests =====

    @Test
    @DisplayName("Should get schedule by veterinarian and day successfully")
    void testGetByVeterinarianAndDay_Success() {
        // Arrange
        when(repository.findByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(testSchedule));
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        ScheduleEntity result = scheduleService.getByVeterinarianAndDay(1L, DayOfWeek.MONDAY);

        // Assert
        assertNotNull(result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
        verify(repository).findByVeterinarianIdAndDayOfWeek(1L, DayOfWeek.MONDAY);
    }

    @Test
    @DisplayName("Should throw NotFoundException when schedule not found")
    void testGetByVeterinarianAndDay_NotFound() {
        // Arrange
        when(repository.findByVeterinarianIdAndDayOfWeek(999L, DayOfWeek.SUNDAY))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> scheduleService.getByVeterinarianAndDay(999L, DayOfWeek.SUNDAY));
    }

    // ===== CREATE Tests =====

    @Test
    @DisplayName("Should create schedule successfully")
    void testCreate_Success() {
        // Arrange
        when(repository.saveAndFlush(any(ScheduleEntity.class))).thenReturn(testSchedule);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        ScheduleEntity result = scheduleService.create(testRequest);

        // Assert
        assertNotNull(result);
        verify(repository).saveAndFlush(any(ScheduleEntity.class));
    }

    // ===== GET BY ID Tests =====

    @Test
    @DisplayName("Should get schedule by id successfully")
    void testGetById_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testSchedule));
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        ScheduleEntity result = scheduleService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should throw NotFoundException when schedule by id not found")
    void testGetById_NotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> scheduleService.getById(999L));
    }

    // ===== UPDATE Tests =====

    @Test
    @DisplayName("Should update schedule successfully")
    void testUpdate_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testSchedule));
        when(repository.saveAndFlush(any(ScheduleEntity.class))).thenReturn(testSchedule);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        ScheduleEntity result = scheduleService.update(1L, testRequest);

        // Assert
        assertNotNull(result);
        verify(repository).saveAndFlush(testSchedule);
    }

    // ===== DELETE Tests =====

    @Test
    @DisplayName("Should delete schedule successfully")
    void testDeleteById_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(testSchedule));
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForDelete(any());
        doNothing().when(validator).validateForDelete(any());
        doNothing().when(repository).deleteById(1L);

        // Act
        ScheduleEntity result = scheduleService.deleteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).deleteById(1L);
    }

    // ===== GET ALL Tests =====

    @Test
    @DisplayName("Should get all schedules successfully")
    void testGetAll_Success() {
        // Arrange
        List<ScheduleEntity> schedules = Collections.singletonList(testSchedule);
        when(repository.findAll()).thenReturn(schedules);
        doNothing().when(permissionValidator).validateForGetAll();

        // Act
        List<ScheduleEntity> result = scheduleService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll();
    }
}
