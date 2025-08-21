package com.razett.toiletmap.repository;

import com.razett.toiletmap.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UsersRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveUser() {
        Users user = Users.builder().username("jiwoo").password(passwordEncoder.encode("240614")).email("kzxn4269@gmail.com").firstName("지원").lastName("정").build();
        userRepository.save(user);
    }
}
