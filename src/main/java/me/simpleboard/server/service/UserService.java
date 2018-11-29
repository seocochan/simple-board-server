package me.simpleboard.server.service;

import me.simpleboard.server.exception.AppException;
import me.simpleboard.server.exception.BadRequestException;
import me.simpleboard.server.exception.ResourceNotFoundException;
import me.simpleboard.server.model.Role;
import me.simpleboard.server.model.enums.RoleName;
import me.simpleboard.server.model.User;
import me.simpleboard.server.payload.LoginRequest;
import me.simpleboard.server.payload.SignUpRequest;
import me.simpleboard.server.payload.UserProfile;
import me.simpleboard.server.repository.RoleRepository;
import me.simpleboard.server.repository.UserRepository;
import me.simpleboard.server.security.JwtTokenProvider;
import me.simpleboard.server.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public Boolean checkUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean checkEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public String authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return tokenProvider.generateToken(authentication);
  }

  public User registerUser(SignUpRequest signUpRequest) {
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException("User Role not set."));
    User user = User.builder()
            .name(signUpRequest.getName())
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .roles(Collections.singleton(userRole))
            .build();

    return userRepository.save(user);
  }

  public void deleteUser(UserPrincipal currentUser, Long userId) {
    if (!userId.equals(currentUser.getId())) {
      throw new BadRequestException("Permission denied");
    }
    userRepository.deleteById(userId);
  }

  public UserProfile getUserProfile(String username) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
  }
}