package ua.edu.ukma.objectanalysis.openvet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserEntity, UserRequest, Long> {

    @Override
    protected UserEntity newEntity() {
        return new UserEntity();
    }
}
