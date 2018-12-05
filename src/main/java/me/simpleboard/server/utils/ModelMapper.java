package me.simpleboard.server.utils;

import me.simpleboard.server.model.*;
import me.simpleboard.server.payload.*;

public class ModelMapper {

  // mapToSummary()
  public static UserSummary mapToSummary(User user) {
    return new UserSummary(user.getId(), user.getUsername(), user.getName());
  }

  public static PostSummary mapToSummary(Post post) {
    return PostSummary.builder()
            .id(post.getId())
            .category(post.getCategory())
            .subCategory(post.getSubCategory())
            .title(post.getTitle())
            .text(post.getText())
            .imageUrl(post.getImageUrl())
            .createdBy(ModelMapper.mapToSummary(post.getUser()))
            .createdAt(post.getCreatedAt())
            .build();
  }

  // map()
  public static PostResponse map(Post post, Boolean isRecommended) {
    return PostResponse.builder()
            .id(post.getId())
            .category(post.getCategory())
            .subCategory(post.getSubCategory())
            .title(post.getTitle())
            .text(post.getText())
            .imageUrl(post.getImageUrl())
            .createdBy(ModelMapper.mapToSummary(post.getUser()))
            .createdAt(post.getCreatedAt())
            .recommends(post.getRecommends().size())
            .isRecommended(isRecommended)
            .build();
  }

  public static CommentResponse map(Comment comment) {
    return CommentResponse.builder()
            .id(comment.getId())
            .text(comment.getText())
            .role(comment.getRole())
            .post(ModelMapper.mapToSummary(comment.getPost()))
            .createdBy(ModelMapper.mapToSummary(comment.getUser()))
            .createdAt(comment.getCreatedAt())
            .build();
  }
}