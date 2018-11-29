package me.simpleboard.server.controller;

import me.simpleboard.server.model.User;
import me.simpleboard.server.payload.ApiResponse;
import me.simpleboard.server.payload.JwtAuthenticationResponse;
import me.simpleboard.server.payload.LoginRequest;
import me.simpleboard.server.payload.SignUpRequest;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    String jwt = userService.authenticateUser(loginRequest);

    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userService.checkUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이름입니다."), HttpStatus.BAD_REQUEST
      );
    }
    if (userService.checkEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(
              new ApiResponse(false, "이미 사용중인 이메일입니다."), HttpStatus.BAD_REQUEST
      );
    }

    User user = userService.registerUser(signUpRequest);

    URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/{username}")
            .buildAndExpand(user.getUsername()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "회원가입에 성공했습니다."));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable Long userId) {
    userService.deleteUser(currentUser, userId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user"));
  }
}