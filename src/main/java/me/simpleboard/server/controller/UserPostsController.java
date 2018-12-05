package me.simpleboard.server.controller;

import me.simpleboard.server.exception.BadRequestException;
import me.simpleboard.server.payload.*;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.PostService;
import me.simpleboard.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/{userId}/posts")
@RequiredArgsConstructor
public class UserPostsController {

  private final PostService postService;

  @PostMapping
  public PostResponse createPost(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long userId,
                                 @Valid @RequestBody PostRequest postRequest) {
    if (!userId.equals(currentUser.getId())) {
      throw new BadRequestException("Permission denied");
    }

    return postService.createPost(currentUser.toUser(), postRequest);
  }

  @GetMapping
  public PagedResponse<PostSummary> getPostsByUser(
          @PathVariable Long userId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return postService.getPostsByUser(userId, page, size);
  }
}