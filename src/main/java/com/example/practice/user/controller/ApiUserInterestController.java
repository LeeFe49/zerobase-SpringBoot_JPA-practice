package com.example.practice.user.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.practice.board.common.model.ResponseResult;
import com.example.practice.board.model.ServiceResult;
import com.example.practice.user.service.UserService;
import com.example.practice.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiUserInterestController {

    private final UserService userService;

    @PutMapping("/api/user/{id}/interest")
    public ResponseEntity<?> interestUser(@PathVariable Long id,
        @RequestHeader("F-TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.addInterestUser(email, id);
        return ResponseResult.result(result);
    }

    @DeleteMapping("/api/user/interest/{id}")
    public ResponseEntity<?> deleteInterestUser(@PathVariable Long id,
        @RequestHeader("F-TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.removeInterestUser(email, id);
        return ResponseResult.result(result);
    }

}
