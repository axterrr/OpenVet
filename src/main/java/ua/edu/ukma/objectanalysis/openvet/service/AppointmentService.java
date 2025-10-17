package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService extends BaseService<AppointmentEntity, AppointmentRequest, Long> {

    @Override
    protected AppointmentEntity newEntity() {
        return new AppointmentEntity();
    }

    public AppointmentEntity cancelByOwner(Long id) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        AppointmentRequest req = AppointmentRequest.builder()
            .status(AppointmentStatus.CANCELED_BY_OWNER)
            .build();
        validator.validateForUpdate(req, entity);
        merger.mergeUpdate(entity, req);
        return repository.saveAndFlush(entity);
    }

    public AppointmentEntity cancelByClinic(Long id) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        AppointmentRequest req = AppointmentRequest.builder()
            .status(AppointmentStatus.CANCELED_BY_CLINIC)
            .build();
        validator.validateForUpdate(req, entity);
        merger.mergeUpdate(entity, req);
        return repository.saveAndFlush(entity);
    }

    public AppointmentEntity markCompleted(Long id) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        AppointmentRequest req = AppointmentRequest.builder()
            .status(AppointmentStatus.COMPLETED)
            .build();
        validator.validateForUpdate(req, entity);
        merger.mergeUpdate(entity, req);
        return repository.saveAndFlush(entity);
    }

    public AppointmentEntity reschedule(Long id, Long newTimeSlotId) {
        if (newTimeSlotId == null) { throw new ValidationException("New time slot id is required"); }
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        AppointmentRequest req = AppointmentRequest.builder()
            .timeSlotId(newTimeSlotId)
            .build();
        validator.validateForUpdate(req, entity);
        merger.mergeUpdate(entity, req);
        return repository.saveAndFlush(entity);
    }
}

