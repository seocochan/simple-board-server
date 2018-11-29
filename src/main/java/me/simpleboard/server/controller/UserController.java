package me.simpleboard.server.controller;

import me.simpleboard.server.payload.UserIdentityAvailability;
import me.simpleboard.server.payload.UserProfile;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.UserService;
import me.simpleboard.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import me.simpleboard.server.payload.UserSummary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    return ModelMapper.mapToSummary(currentUser.toUser());
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam String username) {
    Boolean isAvailable = !userService.checkUsername(username);

    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(@RequestParam String email) {
    Boolean isAvailable = !userService.checkEmail(email);

    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/{username}")
  public UserProfile getUserProfile(@PathVariable String username) {
    return userService.getUserProfile(username);
  }
}