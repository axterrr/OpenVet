package ua.edu.ukma.objectanalysis.openvet.domain.entity.billing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.Identifiable;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.appointment.AppointmentEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentMethod;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "billing")
public class BillingEntity implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "appointment_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppointmentEntity appointment;
}
