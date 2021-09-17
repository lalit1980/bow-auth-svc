package com.bow.auth.controllers;


import java.util.Set;

import javax.validation.Valid;


import com.bow.auth.models.Role;
import com.bow.auth.payload.request.LoginRequest;
import com.bow.auth.payload.request.SignupRequest;
import com.bow.auth.payload.response.JwtResponse;
import com.bow.auth.payload.response.MessageResponse;
import com.bow.auth.security.jwt.services.AppAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    AppAuthService appAuthService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse=appAuthService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (appAuthService.checkUserNameExist(signUpRequest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (appAuthService.checkEmailExist(signUpRequest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<Role> roles=appAuthService.addRoles(signUpRequest);
        String msg=appAuthService.registerUser(signUpRequest,roles);
        return ResponseEntity.ok(new MessageResponse(msg));
    }
}

