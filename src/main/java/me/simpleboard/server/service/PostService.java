package me.simpleboard.server.service;

import me.simpleboard.server.exception.BadRequestException;
import me.simpleboard.server.exception.ResourceNotFoundException;
import me.simpleboard.server.model.Post;
import me.simpleboard.server.model.User;
import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;
import me.simpleboard.server.payload.PostRequest;
import me.simpleboard.server.payload.PostResponse;
import me.simpleboard.server.payload.PostSummary;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.repository.RecommendRepository;
import me.simpleboard.server.repository.PostRepository;
import me.simpleboard.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final RecommendRepository recommendRepository;
  private final UploadService uploadService;

  @Transactional
  public PostResponse createPost(User user, PostRequest postRequest) {
    Post post = Post.builder()
            .category(postRequest.getCategory())
            .subCategory(postRequest.getSubCategory())
            .title(postRequest.getTitle())
            .text(postRequest.getText())
            .imageUrl(postRequest.getImageUrl())
            .user(user)
            .build();

    return ModelMapper.map(postRepository.save(post), false);
  }

  public PostResponse getPostById(Long userId, Long postId) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    Boolean isRecommended = userId == null ? false
            : recommendRepository.existsByUserIdAndPostId(userId, postId);

    return ModelMapper.map(post, isRecommended);
  }

  @Transactional
  public void updatePostById(Long userId, Long postId, PostRequest postRequest) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    if (!userId.equals(post.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    post.setTitle(postRequest.getTitle());
    post.setText(postRequest.getText());
    post.setImageUrl(postRequest.getImageUrl());
  }

  public void removePostById(Long userId, Long postId) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    if (!userId.equals(post.getUser().getId())) {
      throw new BadRequestException("Permission denied");
    }

    postRepository.delete(post);

    if (post.getImageUrl()!=null) {
      uploadService.deleteFile(post.getImageUrl());
    }
  }

  public PagedResponse<PostSummary> getPostsByUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Post> postPage = postRepository.findByUserId(userId, pageable);

    if (postPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), postPage);
    }
    List<PostSummary> postResponses = postPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(postResponses, postPage);
  }

  public PagedResponse<PostSummary> getPostsByCategory(CategoryName category,
                                                       SubCategoryName subCategory,
                                                       int page,
                                                       int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Post> postPage = postRepository.findByCategoryAndSubCategory(category, subCategory, pageable);

    if (postPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), postPage);
    }
    List<PostSummary> postResponses = postPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(postResponses, postPage);
  }

  public PagedResponse<PostSummary> searchPosts(String titleOrText,
                                                int page,
                                                int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Post> postPage = postRepository.findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(titleOrText, titleOrText, pageable);

    if (postPage.getNumberOfElements() == 0) {
      return PagedResponse.of(Collections.emptyList(), postPage);
    }
    List<PostSummary> postResponses = postPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(postResponses, postPage);
  }
}