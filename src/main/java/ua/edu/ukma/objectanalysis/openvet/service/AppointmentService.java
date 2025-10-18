package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.AppointmentStatus;
import ua.edu.ukma.objectanalysis.openvet.dto.appointment.AppointmentRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.ValidationException;
import ua.edu.ukma.objectanalysis.openvet.merger.AppointmentMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.AppointmentValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.AppointmentPermissionValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppointmentService extends BaseService<AppointmentEntity, AppointmentRequest, Long> {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentPermissionValidator permissionValidator;
    private final AppointmentValidator validator;
    private final AppointmentMerger merger;

    public AppointmentService(
        AppointmentRepository repository,
        AppointmentPermissionValidator permissionValidator,
        AppointmentValidator validator,
        AppointmentMerger merger
    ) {
        super(repository, merger, validator, permissionValidator);
        this.appointmentRepository = repository;
        this.permissionValidator = permissionValidator;
        this.validator = validator;
        this.merger = merger;
    }

    @Override
    protected AppointmentEntity newEntity() {
        return new AppointmentEntity();
    }

    @Override
    public AppointmentEntity update(Long id, AppointmentRequest request) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForUpdate(entity);
        if (request != null && request.getStatus() != null) {
            if (request.getStatus() == AppointmentStatus.CANCELED_BY_CLINIC) {
                permissionValidator.validateForClinicCancel(entity);
            } else if (request.getStatus() == AppointmentStatus.CANCELED_BY_OWNER) {
                permissionValidator.validateForOwnerCancel(entity);
            }
        }
        validator.validateForUpdate(request, entity);
        merger.mergeUpdate(entity, request);
        return repository.saveAndFlush(entity);
    }

    public AppointmentEntity cancelByOwner(Long id) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForOwnerCancel(entity);
        AppointmentRequest req = AppointmentRequest.builder()
            .status(AppointmentStatus.CANCELED_BY_OWNER)
            .build();
        validator.validateForUpdate(req, entity);
        merger.mergeUpdate(entity, req);
        return repository.saveAndFlush(entity);
    }

    public AppointmentEntity cancelByClinic(Long id) {
        AppointmentEntity entity = getById(id);
        permissionValidator.validateForClinicCancel(entity);
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

    // ---- Queries ----

    public List<AppointmentEntity> getByPetId(Long petId) {
        List<AppointmentEntity> list = appointmentRepository.findByPetId(petId);
        return filterAllowed(list);
    }

    public List<AppointmentEntity> getByOwnerId(Long ownerId) {
        List<AppointmentEntity> list = appointmentRepository.findByPetOwnerId(ownerId);
        return filterAllowed(list);
    }

    public List<AppointmentEntity> getByVeterinarianId(Long veterinarianId) {
        List<AppointmentEntity> list = appointmentRepository.findByTimeSlotVeterinarianId(veterinarianId);
        return filterAllowed(list);
    }

    public List<AppointmentEntity> getByVeterinarianInRange(Long veterinarianId, LocalDateTime from, LocalDateTime to) {
        List<AppointmentEntity> list = appointmentRepository.findByTimeSlotVeterinarianIdAndTimeSlotStartTimeBetweenOrderByTimeSlotStartTime(veterinarianId, from, to);
        return filterAllowed(list);
    }

    public List<AppointmentEntity> getByStatus(AppointmentStatus status) {
        List<AppointmentEntity> list = appointmentRepository.findByStatus(status);
        return filterAllowed(list);
    }

    private List<AppointmentEntity> filterAllowed(List<AppointmentEntity> list) {
        List<AppointmentEntity> allowed = new ArrayList<>();
        for (AppointmentEntity e : list) {
            try { permissionValidator.validateForGet(e); allowed.add(e); } catch (Exception ignored) {}
        }
        return allowed;
    }
}
