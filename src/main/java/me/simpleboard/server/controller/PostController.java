package me.simpleboard.server.controller;

import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;
import me.simpleboard.server.payload.*;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.PostService;
import lombok.RequiredArgsConstructor;
import me.simpleboard.server.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping
  public PagedResponse<PostSummary> getPostsByCategory(
          @Valid @RequestParam CategoryName category,
          @Valid @RequestParam SubCategoryName subCategory,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return postService.getPostsByCategory(category, subCategory, page, size);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostResponse> getPostById(@CurrentUser UserPrincipal currentUser,
                                                  @PathVariable Long postId) {
    Long userId = currentUser == null ? null : currentUser.getId();
    PostResponse postResponse = postService.getPostById(userId, postId);

    return ResponseEntity.ok(postResponse);
  }

  @PutMapping("/{postId}")
  public ResponseEntity<ApiResponse> updatePostById(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long postId,
                                                    @Valid @RequestBody PostRequest postRequest) {
    postService.updatePostById(currentUser.getId(), postId, postRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully updated a post"));
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<ApiResponse> removePostById(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long postId) {
    postService.removePostById(currentUser.getId(), postId);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a post"));
  }
}