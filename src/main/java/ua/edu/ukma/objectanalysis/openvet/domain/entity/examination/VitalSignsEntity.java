package ua.edu.ukma.objectanalysis.openvet.domain.entity.examination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vital_signs")
public class VitalSignsEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    private int respiratoryRate;

    private BigDecimal weight;

    private BigDecimal temperature;

    private BigDecimal bloodPressure;

    private int heartRate;

    LocalDateTime measuredAt;
}
