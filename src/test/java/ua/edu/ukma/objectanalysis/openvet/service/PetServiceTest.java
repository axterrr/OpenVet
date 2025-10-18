package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.pet.PetEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.PetSex;
import ua.edu.ukma.objectanalysis.openvet.dto.pet.PetRequest;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.pet.PetRepository;
import ua.edu.ukma.objectanalysis.openvet.repository.user.PetOwnerRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.PetPermissionValidator;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PetService Tests")
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetOwnerRepository ownerRepository;

    @Mock
    private BaseMerger<PetEntity, PetRequest> merger;

    @Mock
    private BaseValidator<PetEntity, PetRequest> validator;

    @Mock
    private BasePermissionValidator<PetEntity, PetRequest> permissionValidator;

    @Mock
    private PetPermissionValidator petPermissionValidator;

    @InjectMocks
    private PetService petService;

    private PetOwnerEntity testOwner;
    private PetEntity testPet;
    private PetRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create test data
        testOwner = new PetOwnerEntity();
        testOwner.setId(1L);
        testOwner.setEmail("owner@test.com");
        testOwner.setPhoneNumber("+380123456789");

        testPet = PetEntity.builder()
                .id(1L)
                .name("Fluffy")
                .species("Cat")
                .breed("Persian")
                .birthDate(LocalDate.now().minusYears(2))
                .sex(PetSex.FEMALE)
                .isNeutered(true)
                .owner(testOwner)
                .build();

        testRequest = PetRequest.builder()
                .name("Fluffy")
                .species("Cat")
                .breed("Persian")
                .birthDate(LocalDate.now().minusYears(2))
                .sex(PetSex.FEMALE)
                .neutered(true)
                .ownerPhone("+380123456789") // Using ownerPhone instead of ownerId
                .build();

        // Set up the dependencies inherited from BaseService
        ReflectionTestUtils.setField(petService, "repository", petRepository);
        ReflectionTestUtils.setField(petService, "merger", merger);
        ReflectionTestUtils.setField(petService, "validator", validator);
        ReflectionTestUtils.setField(petService, "permissionValidator", permissionValidator);
    }

    // ===== CREATE Tests =====

    @Test
    @DisplayName("Should create pet successfully")
    void testCreate_Success() {
        // Arrange
        when(petRepository.saveAndFlush(any(PetEntity.class))).thenReturn(testPet);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        PetEntity result = petService.create(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testPet.getId(), result.getId());
        verify(permissionValidator).validateForCreate(testRequest);
        verify(validator).validateForCreate(testRequest);
        verify(petRepository).saveAndFlush(any(PetEntity.class));
    }
}
