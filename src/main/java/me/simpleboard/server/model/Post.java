package me.simpleboard.server.model;

import me.simpleboard.server.model.audit.DateAudit;
import lombok.*;
import me.simpleboard.server.model.enums.CategoryName;
import me.simpleboard.server.model.enums.SubCategoryName;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post extends DateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private CategoryName category;

  @Enumerated(EnumType.STRING)
  private SubCategoryName subCategory;

  @NotBlank
  @Size(max = 40)
  private String title;

  @Size(max = 255)
  private String text;

  private String imageUrl;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(
          mappedBy = "post",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(
          mappedBy = "post",
          cascade = CascadeType.ALL,
          orphanRemoval = true
  )
  @Builder.Default
  private List<Recommend> recommends = new ArrayList<>();
}