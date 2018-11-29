package me.simpleboard.server.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

  @NotBlank
  @Size(min = 2, max = 20)
  private String name;

  @NotBlank
  @Size(min = 3, max = 15)
  private String username;

  @NotBlank
  @Size(max = 40)
  private String email;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;
}
