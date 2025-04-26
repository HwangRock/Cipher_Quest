package com.example.cipherquest.service;

import com.example.cipherquest.dto.LoginRequestDTO;
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
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private JwtProvider jwtProvider;

    public UserEntity userRegister(SignupRequestDTO request){
        String id=request.getUserid();
        String name=request.getUsername();
        String pw=request.getPassword();

        if(userRepository.existsByUserid(id)){
            throw new RuntimeException("이미 존재하는 id입니다.");
        }
        if(userRepository.existsByUsername(name)){
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        if (!name.matches("^.{3,}$")) {
            throw new RuntimeException("닉네임은 최소 3자 이상이어야 합니다.");
        }

        if (!id.matches("^[a-zA-Z0-9]{9,15}$")) {
            throw new RuntimeException("아이디는 영문자와 숫자로 이루어진 9~15자여야 합니다.");
        }

        if (!pw.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-]).{9,15}$")) {
            throw new RuntimeException("비밀번호는 영문자, 숫자, 특수문자를 포함한 9~15자여야 합니다.");
        }

        String encryptedPassword=passwordEncoder.encode(pw);

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

    public String tokenProvider(LoginRequestDTO request){
        String id=request.getUserid();
        String pw=request.getPassword();

        Optional<UserEntity> wantUser=userRepository.findByUserid(id);
        if(wantUser.isEmpty()){
            throw new RuntimeException("ID나 비밀번호를 다시 확인해주세요.");
        }

        if(!passwordEncoder.matches(pw,wantUser.get().getPassword())){
            throw new RuntimeException("ID나 비밀번호를 다시 확인해주세요.");
        }

        Role userRole=wantUser.get().getRole();
        Tier userTier=wantUser.get().getTier();
        String userName=wantUser.get().getUsername();

        String accessToken= jwtProvider.createAccessToken(id,userRole,userTier,userName);

        return accessToken;
    }
}
