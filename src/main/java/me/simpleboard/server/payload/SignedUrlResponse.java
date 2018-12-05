package me.simpleboard.server.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignedUrlResponse {
  private String url;
  private String key;
}
