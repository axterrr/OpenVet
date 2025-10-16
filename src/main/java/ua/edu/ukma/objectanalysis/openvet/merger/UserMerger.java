package ua.edu.ukma.objectanalysis.openvet.merger;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.edu.ukma.objectanalysis.openvet.domain.entity.user.UserEntity;
import ua.edu.ukma.objectanalysis.openvet.dto.user.UserRequest;

import static ua.edu.ukma.objectanalysis.openvet.merger.MergerUtils.ifNotNull;

@Component
@RequiredArgsConstructor
public class UserMerger implements BaseMerger<UserEntity, UserRequest> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void merge(UserEntity entity, UserRequest request) {
        if (request == null) { return; }
        ifNotNull(request.getFirstName(), entity::setFirstName);
        ifNotNull(request.getLastName(), entity::setLastName);
        ifNotNull(request.getRole(), entity::setRole);
        ifNotNull(request.getPhone(), entity::setPhoneNumber);
        ifNotNull(request.getEmail(), entity::setEmail);
        ifNotNull(request.getPassword(), password -> entity.setPassword(passwordEncoder.encode(password)));
    }
}
