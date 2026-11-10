package com.example.userservice.service;

import com.example.userservice.config.JwtUtil;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.response.JwtResponse;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : "ROLE_USER")
                .build();

        User savedUser = userRepository.save(user);

        // Trả về DTO không có password
        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }

    // Logic đăng nhập theo yêu cầu Bài 3
    public LoginResponse login(LoginRequest request) {
        // 1. Tìm User trong Database, nếu không tìm thấy trả về lỗi chung Bad credentials
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        // 2. So khớp mật khẩu bằng BCrypt (matches), không dùng == hoặc equals
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        // 3. Cấp phát JWT qua JwtUtil
        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    public JwtResponse test_username(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return JwtResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(jwtUtil.generateToken(user))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
