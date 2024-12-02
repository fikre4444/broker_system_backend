package com.broker.axumawit.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String getDefaultProfilePicUrl();

  String uploadProfilePic(MultipartFile file);

  byte[] getImage(String fileName);

  String getNameFromUrl(String url);

}
