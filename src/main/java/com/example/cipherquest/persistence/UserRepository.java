package com.example.cipherquest.persistence;

import com.example.cipherquest.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findById(Long id); // 유저 조회를 위함
    Boolean existsByUserid(String userid); // 회원가입 id 중복 방지를 위함
    Boolean existsByUsername(String name); // 회원가입 닉네임 중복 방지를 위함
}
