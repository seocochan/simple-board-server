package me.simpleboard.server.repository;

import me.simpleboard.server.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
  Optional<Recommend> findByUserIdAndPostId(Long userId, Long postId);

  @Query(value = "SELECT post_id FROM recommends WHERE user_id = ?1 ORDER BY created_at DESC",
          nativeQuery = true)
  List<Long> findAllPostIds(Long userId);

  Boolean existsByUserIdAndPostId(Long userId, Long postId);
}