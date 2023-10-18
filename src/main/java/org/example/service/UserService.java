package org.example.service;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserRegistrationRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserRegistrationRequest request) {
        var user = new User(
                request.getName(),
                request.getEmail(),
                encodePassword(request.getPassword())
        );

        userRepository.save(user);
    }

    private static String encodePassword(String password) {
        return Hashing.sha512().hashString(password, UTF_8).toString();
    }
}
