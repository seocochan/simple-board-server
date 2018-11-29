package me.simpleboard.server.service;

import me.simpleboard.server.exception.BadRequestException;
import me.simpleboard.server.exception.ResourceNotFoundException;
import me.simpleboard.server.model.Recommend;
import me.simpleboard.server.model.Post;
import me.simpleboard.server.model.User;
import me.simpleboard.server.payload.PostSummary;
import me.simpleboard.server.payload.PagedResponse;
import me.simpleboard.server.repository.RecommendRepository;
import me.simpleboard.server.repository.PostRepository;
import me.simpleboard.server.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

  private final PostRepository postRepository;
  private final RecommendRepository recommendRepository;

  public void createRecommend(User user, Long postId) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Recommend recommend = new Recommend();
    recommend.setUser(user);
    recommend.setPost(post);
    recommendRepository.save(recommend);
  }

  public void removeRecommend(Long userId, Long postId) {
    Recommend recommend = recommendRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new ResourceNotFoundException("Recommend", "post_id", postId));
    if (!userId.equals(recommend.getUser().getId())) { // FIXME: needless check?
      throw new BadRequestException("Permission denied");
    }

    recommendRepository.delete(recommend);
  }

  public PagedResponse<PostSummary> getPostsRecommendedByUser(Long userId, int page, int size) {
    List<Long> postIds = recommendRepository.findAllPostIds(userId);
    if (postIds.size() == 0) {
      return new PagedResponse<>(Collections.emptyList(), page, size, 0l, 0, true);
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<Post> postPage = postRepository.findByIdInOrderByField(postIds, pageable);

    List<PostSummary> postSummaries = postPage.map(ModelMapper::mapToSummary).getContent();
    return PagedResponse.of(postSummaries, postPage);
  }
}