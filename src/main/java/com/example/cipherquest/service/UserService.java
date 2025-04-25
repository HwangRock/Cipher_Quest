package com.example.cipherquest.service;

import com.example.cipherquest.dto.SignupRequestDTO;
import com.example.cipherquest.model.Role;
import com.example.cipherquest.model.Tier;
import com.example.cipherquest.model.UserEntity;
import com.example.cipherquest.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity userRegister(SignupRequestDTO request){
        String id=request.getUserid();
        String name=request.getUsername();

        if(userRepository.existsByUserid(id)){
            throw new RuntimeException("already exist user id.");
        }
        if(userRepository.existsByUsername(name)){
            throw new RuntimeException("already exist user name.");
        }

        String encryptedPassword=passwordEncoder.encode(request.getPassword());

        UserEntity entity=UserEntity.builder()
                .userid(id)
                .score(0)
                .username(name)
                .password(encryptedPassword)
                .tier(Tier.BEGINNER)
                .role(Role.USER)
                .createdate(LocalDateTime.now())
                .build();

        return userRepository.save(entity);
    }
}
