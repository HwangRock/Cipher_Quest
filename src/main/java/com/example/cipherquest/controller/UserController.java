package com.example.cipherquest.controller;

import com.example.cipherquest.dto.*;
import com.example.cipherquest.model.UserEntity;
import com.example.cipherquest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/userSignup")
    public ResponseEntity<?> userSignup(@RequestBody SignupRequestDTO request){
        UserEntity result=userService.userRegister(request);

        SignupResponseDTO response=SignupResponseDTO.builder()
                .id(result.getId())
                .userid(result.getUserid())
                .username(result.getUsername())
                .build();

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequestDTO request){
        String accessToken=userService.tokenProvider(request);
        String refreshToken=userService.refreshTokenProvider(request.getUserid());

        LoginResponseDTO response=LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> userWithdraw(@RequestBody LoginRequestDTO request){
        String name=userService.userWithdraw(request);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(name+" 유저가 탈퇴됐습니다.")
                        .build()
        );

    }

    @PostMapping("againTokenProvide")
    public ResponseEntity<?> againTokenProvide(@RequestBody TokenProvideRequestDTO request){
        String accessToken=userService.tokenAgainProvider(request.getUserid(),request.getRefreshToken());

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(accessToken)
                        .build()
        );
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestDTO request){
        String name=userService.updatePassword(request);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(name+" 유저의 비밀번호가 변경됐습니다.")
                        .build()
        );
    }
}
