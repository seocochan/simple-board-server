package me.simpleboard.server.repository;

import me.simpleboard.server.model.Post;
import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findByCategoryAndSubCategory(CategoryName category, SubCategoryName subCategory, Pageable pageable);

  Page<Post> findByUserId(Long userId, Pageable pageable);

  @Query(value = "SELECT * FROM posts WHERE id IN ?1 ORDER BY FIELD(id, ?1)",
          countQuery = "SELECT COUNT(*) FROM posts WHERE id IN ?1",
          nativeQuery = true)
  Page<Post> findByIdInOrderByField(List<Long> id, Pageable pageable);

  Page<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(String title, String text, Pageable pageable);
}