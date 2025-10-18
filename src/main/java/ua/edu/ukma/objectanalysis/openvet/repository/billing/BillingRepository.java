package ua.edu.ukma.objectanalysis.openvet.repository.billing;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.List;

public interface BillingRepository extends BaseRepository<BillingEntity, Long> {
    List<BillingEntity> findAllByAppointment_Pet_Owner_Id(Long ownerId);
}
