package com.bow.auth.security.jwt.services;

import com.bow.auth.models.ERole;
import com.bow.auth.models.Role;
import com.bow.auth.models.Users;
import com.bow.auth.payload.request.LoginRequest;
import com.bow.auth.payload.request.SignupRequest;
import com.bow.auth.payload.response.JwtResponse;
import com.bow.auth.repository.RoleRepository;
import com.bow.auth.repository.UserRepository;
import com.bow.auth.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppAuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public String registerUser(SignupRequest signUpRequest,Set<Role> roles) {
        StringBuffer msg=new StringBuffer();
        // Create new user's account
        Users user = new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);

        return msg.append("User registered successfully!").toString();
    }

    public boolean checkEmailExist(SignupRequest signUpRequest){
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return true;
        }else {
            return false;
        }
    }

    public boolean checkUserNameExist(SignupRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return true;
        }else {
            return false;
        }
    }
    public Set<Role> addRoles(SignupRequest signUpRequest){

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Optional<Role> adminRole1 = roleRepository.findByName(ERole.ROLE_ADMIN);
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Optional<Role> modRole1 = roleRepository.findByName(ERole.ROLE_MODERATOR);
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Optional<Role> userRole1 = roleRepository.findByName(ERole.ROLE_USER);
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }
}
