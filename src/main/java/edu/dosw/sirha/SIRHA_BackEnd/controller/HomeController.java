package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
  @GetMapping("/")
  public ResponseEntity<Void> redirectToSwagger() {
    return ResponseEntity
        .status(302)
        .header("Location", "/swagger-ui/index.html")
        .build();
  }
}
