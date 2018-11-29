package me.simpleboard.server.payload;

import lombok.Getter;
import lombok.Setter;
import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostRequest {
  private CategoryName category;
  private SubCategoryName subCategory;

  @NotBlank
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String text;

  private String imageUrl;
}