package ua.edu.ukma.objectanalysis.openvet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.LabResultEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.MedicalRecordsEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.PrescriptionEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.examination.VitalSignsEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.LabResultResponse;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.MedicalRecordsResponse;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.PrescriptionResponse;
import ua.edu.ukma.objectanalysis.openvet.dto.examination.VitalSignsResponse;
import ua.edu.ukma.objectanalysis.openvet.service.MedicalRecordsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/medical-records")
public class MedicalRecordsController {

    private final MedicalRecordsService medicalRecordsService;

    @GetMapping("")
    public ResponseEntity<List<MedicalRecordsResponse>> getUserList() {
        return new ResponseEntity<>(medicalRecordsService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody MedicalRecordsRequest request) {
        medicalRecordsService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<MedicalRecordsResponse> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(map(medicalRecordsService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody MedicalRecordsRequest request) {
        medicalRecordsService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        medicalRecordsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private MedicalRecordsResponse map(MedicalRecordsEntity entity) {
        if (entity == null) { return null; }
        return MedicalRecordsResponse.builder()
            .id(entity.getId())
            .appointmentId(entity.getAppointment().getId())
            .diagnosis(entity.getDiagnosis())
            .treatment(entity.getTreatment())
            .notes(entity.getNotes())
            .vitalSigns(map(entity.getVitalSigns()))
            .labResults(entity.getLabResults().stream().map(this::map).toList())
            .prescriptions(entity.getPrescriptions().stream().map(this::map).toList())
            .build();
    }

    private VitalSignsResponse map(VitalSignsEntity entity) {
        return VitalSignsResponse.builder()
            .id(entity.getId())
            .medicalRecordId(entity.getMedicalRecord().getId())
            .respiratoryRate(entity.getRespiratoryRate())
            .weight(entity.getWeight())
            .temperature(entity.getTemperature())
            .bloodPressure(entity.getBloodPressure())
            .heartRate(entity.getHeartRate())
            .build();
    }

    private LabResultResponse map(LabResultEntity entity) {
        return LabResultResponse.builder()
            .id(entity.getId())
            .medicalRecordId(entity.getMedicalRecord().getId())
            .testType(entity.getTestType())
            .resultDetails(entity.getResultDetails())
            .notes(entity.getNotes())
            .build();
    }

    private PrescriptionResponse map(PrescriptionEntity entity) {
        return PrescriptionResponse.builder()
            .id(entity.getId())
            .medicalRecordId(entity.getMedicalRecord().getId())
            .medicineName(entity.getMedicationName())
            .dosage(entity.getDosage())
            .frequency(entity.getFrequency())
            .duration(entity.getDuration())
            .instructions(entity.getInstructions())
            .build();
    }
}
