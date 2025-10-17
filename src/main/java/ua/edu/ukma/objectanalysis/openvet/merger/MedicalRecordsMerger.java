package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.LabResultEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.PrescriptionEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.VitalSignsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.LabResultRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.PrescriptionRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.VitalSignsRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.repository.appointment.AppointmentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        ifNotNull(request.getVitalSigns(), vitalSigns -> setVitalSigns(entity, vitalSigns));
        ifNotNull(request.getPrescriptions(), prescriptions -> setPrescriptions(entity, prescriptions));
        ifNotNull(request.getLabResults(), labResults -> setLabResults(entity, labResults));
    }

    private void setVitalSigns(MedicalRecordsEntity entity, VitalSignsRequest request) {
        VitalSignsEntity vital = entity.getVitalSigns();
        if (vital == null) {
            vital = new VitalSignsEntity();
            vital.setMedicalRecord(entity);
            entity.setVitalSigns(vital);
        }

        vital.setRespiratoryRate(request.getRespiratoryRate());
        vital.setWeight(request.getWeight());
        vital.setTemperature(request.getTemperature());
        vital.setBloodPressure(request.getBloodPressure());
        vital.setHeartRate(request.getHeartRate());
    }

    private void setPrescriptions(MedicalRecordsEntity entity, List<PrescriptionRequest> prescriptionRequests) {
        Set<PrescriptionEntity> prescriptionEntities = entity.getPrescriptions();
        if (prescriptionEntities == null) {
            prescriptionEntities = new HashSet<>();
            entity.setPrescriptions(prescriptionEntities);
        } else {
            prescriptionEntities.clear();
        }

        for (PrescriptionRequest request : prescriptionRequests) {
            PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder()
                .medicalRecord(entity)
                .medicationName(request.getMedicineName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .duration(request.getDuration())
                .instructions(request.getInstructions())
                .build();
            prescriptionEntities.add(prescriptionEntity);
        }
    }

    private void setLabResults(MedicalRecordsEntity entity, List<LabResultRequest> labResultRequests) {
        Set<LabResultEntity> labResultEntities = entity.getLabResults();
        if (labResultEntities == null) {
            labResultEntities = new HashSet<>();
            entity.setLabResults(labResultEntities);
        } else {
            labResultEntities.clear();
        }

        for (LabResultRequest request : labResultRequests) {
            LabResultEntity labResultEntity = LabResultEntity.builder()
                .medicalRecord(entity)
                .testType(request.getTestType())
                .resultDetails(request.getResultDetails())
                .notes(request.getNotes())
                .build();
            labResultEntities.add(labResultEntity);
        }
    }
}
