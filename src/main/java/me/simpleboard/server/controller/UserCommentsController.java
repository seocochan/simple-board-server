package me.simpleboard.server.controller;

import me.simpleboard.server.payload.CommentResponse;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.service.CommentService;
import me.simpleboard.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/comments")
@RequiredArgsConstructor
public class UserCommentsController {

  private final CommentService commentService;

  @GetMapping
  public PagedResponse<CommentResponse> getCommentsByUser(
          @PathVariable Long userId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return commentService.getCommentsByUser(userId, page, size);
  }
}