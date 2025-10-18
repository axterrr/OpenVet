package ua.edu.ukma.objectanalysis.openvet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.AdminEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.PetOwnerEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.VeterinarianEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.exception.NotFoundException;
import ua.edu.ukma.objectanalysis.openvet.merger.BaseMerger;
import ua.edu.ukma.objectanalysis.openvet.repository.user.UserRepository;
import ua.edu.ukma.objectanalysis.openvet.validator.data.BaseValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.BasePermissionValidator;
import ua.edu.ukma.objectanalysis.openvet.validator.permission.UserPermissionValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPermissionValidator userPermissionValidator;

    @Mock
    private BaseMerger<UserEntity, UserRequest> merger;

    @Mock
    private BaseValidator<UserEntity, UserRequest> validator;

    @Mock
    private BasePermissionValidator<UserEntity, UserRequest> permissionValidator;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private UserRequest petOwnerRequest;
    private UserRequest veterinarianRequest;
    private UserRequest adminRequest;

    @BeforeEach
    void setUp() {
        testUser = new PetOwnerEntity();
        testUser.setId(1L);
        testUser.setEmail("owner@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhoneNumber("+380123456789");

        petOwnerRequest = UserRequest.builder()
                .email("owner@test.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phone("+380123456789")
                .role(UserRole.PET_OWNER)
                .build();

        veterinarianRequest = UserRequest.builder()
                .email("vet@test.com")
                .password("password123")
                .firstName("Jane")
                .lastName("Smith")
                .phone("+380987654321")
                .role(UserRole.VETERINARIAN)
                .build();

        adminRequest = UserRequest.builder()
                .email("admin@test.com")
                .password("password123")
                .firstName("Admin")
                .lastName("User")
                .phone("+380111111111")
                .role(UserRole.ADMIN)
                .build();

        // Set up the dependencies inherited from BaseService
        ReflectionTestUtils.setField(userService, "repository", userRepository);
        ReflectionTestUtils.setField(userService, "merger", merger);
        ReflectionTestUtils.setField(userService, "validator", validator);
        ReflectionTestUtils.setField(userService, "permissionValidator", permissionValidator);
    }

    // ===== CREATE PET OWNER Tests =====

    @Test
    @DisplayName("Should create pet owner successfully")
    void testCreate_PetOwner_Success() {
        // Arrange
        PetOwnerEntity petOwner = new PetOwnerEntity();
        petOwner.setId(1L);
        when(userRepository.saveAndFlush(any(PetOwnerEntity.class))).thenReturn(petOwner);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        UserEntity result = userService.create(petOwnerRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof PetOwnerEntity);
        verify(permissionValidator).validateForCreate(petOwnerRequest);
        verify(validator).validateForCreate(petOwnerRequest);
        verify(merger).mergeCreate(any(PetOwnerEntity.class), eq(petOwnerRequest));
        verify(userRepository).saveAndFlush(any(PetOwnerEntity.class));
    }

    // ===== CREATE VETERINARIAN Tests =====

    @Test
    @DisplayName("Should create veterinarian successfully")
    void testCreate_Veterinarian_Success() {
        // Arrange
        VeterinarianEntity vet = new VeterinarianEntity();
        vet.setId(2L);
        when(userRepository.saveAndFlush(any(VeterinarianEntity.class))).thenReturn(vet);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        UserEntity result = userService.create(veterinarianRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VeterinarianEntity);
        verify(permissionValidator).validateForCreate(veterinarianRequest);
        verify(validator).validateForCreate(veterinarianRequest);
        verify(merger).mergeCreate(any(VeterinarianEntity.class), eq(veterinarianRequest));
        verify(userRepository).saveAndFlush(any(VeterinarianEntity.class));
    }

    // ===== CREATE ADMIN Tests =====

    @Test
    @DisplayName("Should create admin successfully")
    void testCreate_Admin_Success() {
        // Arrange
        AdminEntity admin = new AdminEntity();
        admin.setId(3L);
        when(userRepository.saveAndFlush(any(AdminEntity.class))).thenReturn(admin);
        doNothing().when(permissionValidator).validateForCreate(any());
        doNothing().when(validator).validateForCreate(any());
        doNothing().when(merger).mergeCreate(any(), any());

        // Act
        UserEntity result = userService.create(adminRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof AdminEntity);
        verify(permissionValidator).validateForCreate(adminRequest);
        verify(validator).validateForCreate(adminRequest);
        verify(merger).mergeCreate(any(AdminEntity.class), eq(adminRequest));
        verify(userRepository).saveAndFlush(any(AdminEntity.class));
    }

    // ===== GET BY ROLE Tests =====

    @Test
    @DisplayName("Should get users by role successfully")
    void testGetByRole_Success() {
        // Arrange
        List<UserEntity> users = Arrays.asList(testUser);
        when(userRepository.findAllByRole(UserRole.PET_OWNER)).thenReturn(users);
        doNothing().when(userPermissionValidator).validateForGetByRole(UserRole.PET_OWNER);

        // Act
        List<UserEntity> result = userService.getByRole(UserRole.PET_OWNER);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userPermissionValidator).validateForGetByRole(UserRole.PET_OWNER);
        verify(userRepository).findAllByRole(UserRole.PET_OWNER);
    }

    @Test
    @DisplayName("Should return empty list when no users with role found")
    void testGetByRole_EmptyList() {
        // Arrange
        when(userRepository.findAllByRole(UserRole.VETERINARIAN)).thenReturn(Arrays.asList());
        doNothing().when(userPermissionValidator).validateForGetByRole(UserRole.VETERINARIAN);

        // Act
        List<UserEntity> result = userService.getByRole(UserRole.VETERINARIAN);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository).findAllByRole(UserRole.VETERINARIAN);
    }

    // ===== GET BY ID Tests =====

    @Test
    @DisplayName("Should get user by id successfully")
    void testGetById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(permissionValidator).validateForGet(any());

        // Act
        UserEntity result = userService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("owner@test.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(permissionValidator).validateForGet(testUser);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user not found")
    void testGetById_NotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getById(999L));
        verify(userRepository).findById(999L);
    }

    // ===== UPDATE Tests =====

    @Test
    @DisplayName("Should update user successfully")
    void testUpdate_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(testUser);
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForUpdate(any());
        doNothing().when(validator).validateForUpdate(any(), any());
        doNothing().when(merger).mergeUpdate(any(), any());

        // Act
        UserEntity result = userService.update(1L, petOwnerRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(permissionValidator).validateForUpdate(testUser);
        verify(validator).validateForUpdate(petOwnerRequest, testUser);
        verify(merger).mergeUpdate(testUser, petOwnerRequest);
        verify(userRepository).saveAndFlush(testUser);
    }

    // ===== DELETE Tests =====

    @Test
    @DisplayName("Should delete user by id successfully")
    void testDeleteById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(permissionValidator).validateForGet(any());
        doNothing().when(permissionValidator).validateForDelete(any());
        doNothing().when(validator).validateForDelete(any());
        doNothing().when(userRepository).deleteById(1L);

        // Act
        UserEntity result = userService.deleteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).deleteById(1L);
    }

    // ===== GET ALL Tests =====

    @Test
    @DisplayName("Should get all users successfully")
    void testGetAll_Success() {
        // Arrange
        List<UserEntity> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        doNothing().when(permissionValidator).validateForGetAll();

        // Act
        List<UserEntity> result = userService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
        verify(permissionValidator).validateForGetAll();
    }
}
