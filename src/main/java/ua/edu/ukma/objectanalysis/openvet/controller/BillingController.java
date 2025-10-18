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
import ua.edu.ukma.objectanalysis.openvet.domain.entity.billing.BillingEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.billing.BillingResponse;
import ua.edu.ukma.objectanalysis.openvet.service.BillingService;

import java.util.List;

@RestController
@RequestMapping("api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("")
    public ResponseEntity<List<BillingResponse>> getBillings() {
        return new ResponseEntity<>(billingService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> create(@RequestBody BillingRequest request) {
        billingService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<BillingResponse> getBilling(@PathVariable Long id) {
        return new ResponseEntity<>(map(billingService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody BillingRequest request) {
        billingService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        billingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("pet-owner/{ownerId}")
    public ResponseEntity<List<BillingResponse>> getBillingsByPetOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(billingService.getByPetOwner(ownerId).stream().map(this::map).toList(), HttpStatus.OK);
    }

    private BillingResponse map(BillingEntity entity) {
        if (entity == null) { return null; }
        return BillingResponse.builder()
            .id(entity.getId())
            .amount(entity.getAmount())
            .currency(entity.getCurrency())
            .paymentMethod(entity.getPaymentMethod())
            .paymentStatus(entity.getPaymentStatus())
            .appointmentId(entity.getAppointment() != null ? entity.getAppointment().getId() : null)
            .build();
    }
}
