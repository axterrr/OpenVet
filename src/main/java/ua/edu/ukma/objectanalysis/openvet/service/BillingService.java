package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.repository.billing.BillingRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BillingPermissionValidator;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BillingService extends BaseService<BillingEntity, BillingRequest, Long> {

    private final BillingRepository billingRepository;
    private final BillingPermissionValidator permissionValidator;

    @Override
    protected BillingEntity newEntity() {
        return new BillingEntity();
    }

    public List<BillingEntity> getByPetOwner(Long ownerId) {
        permissionValidator.validateForGetByPetOwner(ownerId);
        return billingRepository.findAllByAppointment_Pet_Owner_Id(ownerId);
    }
}
