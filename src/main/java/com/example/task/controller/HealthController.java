// src/main/java/com/example/task/controller/HealthController.java
package com.example.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping("/health")
  public String ok(){ return "ok"; }
}
