package ua.edu.ukma.objectanalysis.openvet.merger.examination;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class MedicalRecordsMerger implements BaseMerger<MedicalRecordsEntity, MedicalRecordsRequest> {

    private final AppointmentRepository appointmentRepository;

    @Override
    public void mergeCreate(MedicalRecordsEntity entity, MedicalRecordsRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);

        if (request.getAppointmentId() != null) {
            entity.setAppointment(appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + request.getAppointmentId()))
            );
        }
    }

    @Override
    public void mergeUpdate(MedicalRecordsEntity entity, MedicalRecordsRequest request) {
        if (request == null || entity == null) { return; }
        commonMerge(entity, request);
    }

    private void commonMerge(MedicalRecordsEntity entity, MedicalRecordsRequest request) {
        ifNotNull(request.getDiagnosis(), entity::setDiagnosis);
        ifNotNull(request.getTreatment(), entity::setTreatment);
        ifNotNull(request.getNotes(), entity::setNotes);
    }
}
