package com.razett.toiletmap.service;

import com.razett.toiletmap.entity.Users;
import com.razett.toiletmap.model.LoginUser;
import com.razett.toiletmap.model.UsersDTO;
import com.razett.toiletmap.model.mapper.UserMapper;
import com.razett.toiletmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersDTO usersDTO = userMapper.toDTO(userRepository.findByUsername(username));
        if (usersDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new LoginUser(usersDTO);
    }

    // 비밀번호 변경 메서드 추가
    public void updatePassword(String username, String rawPassword) {
        UsersDTO usersDTO = userMapper.toDTO(userRepository.findByUsername(username));

        if (usersDTO != null) {
            usersDTO.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(userMapper.toEntity(usersDTO));
        }
    }
}
