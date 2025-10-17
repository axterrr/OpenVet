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
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.domain.enums.UserRole;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserResponse;
import ua.edu.ukma.objectanalysis.openvet.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUserList() {
        return new ResponseEntity<>(userService.getAll().stream().map(this::map).toList(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody UserRequest request) {
        userService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(map(userService.getById(id)), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody UserRequest request) {
        userService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("admins")
    public ResponseEntity<List<UserResponse>> getAdminList() {
        return new ResponseEntity<>(userService.getByRole(UserRole.ADMIN).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("veterinarians")
    public ResponseEntity<List<UserResponse>> getVeterinariansList() {
        return new ResponseEntity<>(userService.getByRole(UserRole.VETERINARIAN).stream().map(this::map).toList(), HttpStatus.OK);
    }

    @GetMapping("pet-owners")
    public ResponseEntity<List<UserResponse>> getPetOwnerList() {
        return new ResponseEntity<>(userService.getByRole(UserRole.PET_OWNER).stream().map(this::map).toList(), HttpStatus.OK);
    }

    private UserResponse map(UserEntity entity) {
        if (entity == null) { return null; }
        return UserResponse.builder()
            .id(entity.getId())
            .phone(entity.getPhoneNumber())
            .email(entity.getEmail())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .role(entity.getRole())
            .build();
    }
}
