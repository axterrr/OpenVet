package ua.edu.ukma.objectanalysis.openvet.repository.appointment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends BaseRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByPetId(Long petId);
    List<AppointmentEntity> findByPetOwnerId(Long ownerId);
    List<AppointmentEntity> findByTimeSlotVeterinarianId(Long veterinarianId);
    List<AppointmentEntity> findByTimeSlotVeterinarianIdAndTimeSlotStartTimeBetweenOrderByTimeSlotStartTime(Long veterinarianId, LocalDateTime from, LocalDateTime to);
    List<AppointmentEntity> findByStatus(AppointmentStatus status);

    @Query("""
        select a from AppointmentEntity a
        where a.status = :appointmentStatus
        and a.timeSlot.startTime between :startOfDay and :endOfDay
    """)
    List<AppointmentEntity> findByDate(
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay,
        @Param("appointmentStatus") AppointmentStatus appointmentStatus
    );
}
