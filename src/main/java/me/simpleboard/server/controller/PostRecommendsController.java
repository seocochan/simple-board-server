package me.simpleboard.server.controller;

import me.simpleboard.server.payload.ApiResponse;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/recommends")
@RequiredArgsConstructor
public class PostRecommendsController {

  private final RecommendService createBookmark;

  @PostMapping
  public ResponseEntity<ApiResponse> createRecommend(@CurrentUser UserPrincipal currentUser,
                                                     @PathVariable Long postId) {
    createBookmark.createRecommend(currentUser.toUser(), postId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a recommend"));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse> removeRecommend(@CurrentUser UserPrincipal currentUser,
                                                     @PathVariable Long postId) {
    createBookmark.removeRecommend(currentUser.getId(), postId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a recommend"));
  }
}