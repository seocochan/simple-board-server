package me.simpleboard.server.controller;

import me.simpleboard.server.payload.PostSummary;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.service.RecommendService;
import me.simpleboard.server.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/recommends")
@RequiredArgsConstructor
public class UserRecommendsController {

  private final RecommendService recommendService;

  @GetMapping
  public PagedResponse<PostSummary> getPostsRecommendedByUser(
          @PathVariable Long userId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    return recommendService.getPostsRecommendedByUser(userId, page, size);
  }
}