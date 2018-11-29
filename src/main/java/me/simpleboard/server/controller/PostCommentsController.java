package me.simpleboard.server.controller;

import me.simpleboard.server.payload.ApiResponse;
import me.simpleboard.server.payload.CommentRequest;
import me.simpleboard.server.payload.CommentResponse;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.CommentService;
import me.simpleboard.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentsController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<ApiResponse> createComment(@CurrentUser UserPrincipal currentUser,
                                                   @PathVariable Long postId,
                                                   @Valid @RequestBody CommentRequest commentRequest) {
    commentService.createComment(currentUser.toUser(), postId, commentRequest);

    return ResponseEntity.ok(new ApiResponse(true, "Successfully created a comment"));
  }

  @GetMapping
  public PagedResponse<CommentResponse> getCommentsByPost(
          @PathVariable Long postId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return commentService.getCommentsByPost(postId, page, size);
  }
}