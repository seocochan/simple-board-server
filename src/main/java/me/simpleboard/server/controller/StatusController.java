package me.simpleboard.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

  @GetMapping("/status")
  public ResponseEntity<String> checkStatus() {
    return ResponseEntity.ok("ok");
  }
}