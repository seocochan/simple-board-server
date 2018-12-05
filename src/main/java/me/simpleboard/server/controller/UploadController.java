package me.simpleboard.server.controller;

import lombok.RequiredArgsConstructor;
import me.simpleboard.server.payload.ApiResponse;
import me.simpleboard.server.payload.SignedUrlResponse;
import me.simpleboard.server.security.CurrentUser;
import me.simpleboard.server.security.UserPrincipal;
import me.simpleboard.server.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

  private final UploadService uploadService;

  @GetMapping("/signedUrl")
  public ResponseEntity<SignedUrlResponse> getSignedUrl(@CurrentUser UserPrincipal currentUser,
                                                        @RequestParam(required = false) String key) {
    System.out.println(key);
    return ResponseEntity.ok(uploadService.getSignedUrl(currentUser.getId().toString(), key));
  }

  @DeleteMapping("/files")
  public ResponseEntity<ApiResponse> deleteFile(@RequestParam String key) {
    uploadService.deleteFile(key);
    return ResponseEntity.ok(new ApiResponse(true, "Successfully removed a image"));
  }
}