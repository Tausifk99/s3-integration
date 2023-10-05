package com.s3.web.controller;

import com.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Controller {

  @Autowired
  private S3Service s3Service;

  @PostMapping("put-file")
  public ResponseEntity<String> uploadFile(@RequestParam String bucketName, @RequestParam("file") MultipartFile file) {
    String response = s3Service.uploadFile(bucketName, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("get-file/{file}")
  public ResponseEntity<byte[]> getFile(@RequestBody String bucketName, @PathVariable("file") String fileName) {
    byte[] fileData = s3Service.getFile(bucketName, fileName);
    String documentName = s3Service.getDocumentName(fileName);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + documentName);
    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
    headers.setContentType(mediaType);
    return ResponseEntity.ok().headers(headers).body(fileData);
  }

  @DeleteMapping("delete/{file}")
  public ResponseEntity<String> deleteFile(@RequestBody String bucketName, @PathVariable("file") String fileName) {
    String response = s3Service.deleteFile(bucketName, fileName);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
