package me.simpleboard.server.payload;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {
  private Long id;
  private String text;
  private String role;
  private PostSummary post;
  private UserSummary createdBy;
  private Instant createdAt;
}
