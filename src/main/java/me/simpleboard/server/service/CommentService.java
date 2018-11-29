package me.simpleboard.server.service;

import me.simpleboard.server.exception.BadRequestException;
import me.simpleboard.server.exception.ResourceNotFoundException;
import me.simpleboard.server.model.Post;
import me.simpleboard.server.model.Comment;
import me.simpleboard.server.model.User;
import me.simpleboard.server.payload.CommentRequest;
import me.simpleboard.server.payload.CommentResponse;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.repository.PostRepository;
import me.simpleboard.server.repository.CommentRepository;
import me.simpleboard.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public void createComment(User user, Long postId, CommentRequest commentRequest) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Comment comment = Comment.builder()
            .user(user)
            .post(post)
            .text(commentRequest.getText())
            .build();
    commentRepository.save(comment);
  }

  @Transactional
  public void updateCommentById(Long userId, Long commentId, CommentRequest commentRequest) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    comment.setText(commentRequest.getText());
  }

  public void removeCommentById(Long userId, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    if (!userId.equals(comment.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    commentRepository.delete(comment);
  }

  public PagedResponse<CommentResponse> getCommentsByUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Comment> commentPage = commentRepository.findByUserId(userId, pageable);

    if (commentPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), commentPage);
    }
    List<CommentResponse> commentResponses = commentPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(commentResponses, commentPage);
  }

  public PagedResponse<CommentResponse> getCommentsByPost(Long postId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

    if (commentPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), commentPage);
    }
    List<CommentResponse> commentResponses = commentPage.map(ModelMapper::map).getContent();
    return PagedResponse.of(commentResponses, commentPage);
  }
}