package com.razett.toiletmap;

import com.razett.toiletmap.entity.Users;
import com.razett.toiletmap.repository.UserRepository;
import com.razett.toiletmap.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ToiletMapApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

//    @Test
//    void contextLoads() {
//        Users jiwon = Users.builder().username("jiwon").password("240614").firstName("지원").lastName("정").email("kzxn4269@gmail.com").build();
//        Users jiwoo = Users.builder().username("jiwoo").password("240614").firstName("지우").lastName("최").email("1127wldn@naver.com").build();
//
//        jiwon.setPassword(passwordEncoder.encode(jiwon.getPassword()));
//        jiwoo.setPassword(passwordEncoder.encode(jiwoo.getPassword()));
//        userRepository.save(jiwon);
//        userRepository.save(jiwoo);
//    }

}
