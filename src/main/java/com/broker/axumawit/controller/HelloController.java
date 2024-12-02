package com.broker.axumawit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broker.axumawit.service.aws.S3Service;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

  @Autowired
  private S3Service s3Service;

  @GetMapping("say-hello")
  public String sayHello() {
    s3Service.listBuckets();
    // s3Service.uploadFile("profilePics/default.jpg",
    // "C:\\Users\\hpc\\profilePics\\default.jpg");
    // s3Service.createFolder("profilePics/");
    return "Hello Sir!";
  }

}
