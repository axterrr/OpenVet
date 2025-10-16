package ua.edu.ukma.objectanalysis.openvet.repository.user;

import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.repository.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhoneNumber(String phone);
    boolean existsByPhoneNumber(String phone);
    boolean existsByEmail(String email);
}
