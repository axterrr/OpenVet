package ua.edu.ukma.objectanalysis.openvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.billing.BillingRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BillingPermissionValidator;

import java.util.List;

@Service
@Transactional
public class BillingService extends BaseService<BillingEntity, BillingRequest, Long> {

    private final BillingRepository billingRepository;
    private final BillingPermissionValidator permissionValidator;


    public BillingService(
        BaseRepository<BillingEntity, Long> repository,
        BaseMerger<BillingEntity, BillingRequest> merger,
        BaseValidator<BillingEntity, BillingRequest> validator,
        BillingPermissionValidator permissionValidator,
        BillingRepository billingRepository
    ) {
        super(repository, merger, validator, permissionValidator);
        this.permissionValidator = permissionValidator;
        this.billingRepository = billingRepository;
    }

    @Override
    protected BillingEntity newEntity() {
        return new BillingEntity();
    }

    public List<BillingEntity> getByPetOwner(Long ownerId) {
        permissionValidator.validateForGetByPetOwner(ownerId);
        return billingRepository.findAllByAppointment_Pet_Owner_Id(ownerId);
    }
}
