package ua.edu.ukma.objectanalysis.openvet.repository;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;

import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
