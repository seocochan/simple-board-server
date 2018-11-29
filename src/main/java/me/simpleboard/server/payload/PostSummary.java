package me.simpleboard.server.payload;

import lombok.*;
import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSummary {
  private Long id;
  private CategoryName category;
  private SubCategoryName subCategory;
  private String title;
  private String text;
  private String imageUrl;
  private UserSummary createdBy;
}