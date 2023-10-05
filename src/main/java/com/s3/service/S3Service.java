package com.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service {

  @Autowired
  private AmazonS3 amazonS3;

  public String uploadFile(String bucketName, MultipartFile multipartFile) {
    try {
      File file = new File(multipartFile.getOriginalFilename());
      multipartFile.transferTo(file.getAbsoluteFile());
      PutObjectResult result = amazonS3.putObject(bucketName, multipartFile.getOriginalFilename(), file);
      return result.getContentMd5();
    } catch (IOException e) {
      log.error("unable to upload file to s3 object, fileName :" + multipartFile.getOriginalFilename());
      return "";
    }
  }

  public byte[] getFile(String bucketName, String fileName) {
    S3Object s3Object = amazonS3.getObject(bucketName, fileName);
    S3ObjectInputStream objectContent = s3Object.getObjectContent();
    try {
      return IOUtils.toByteArray(objectContent);
    } catch (IOException e) {
      log.error("unable to get file from s3 bucket, bucket: " + bucketName + " and file: " + fileName);
      return null;
    }
  }

  public String deleteFile(String bucketName, String fileName) {
    amazonS3.deleteObject(bucketName, fileName);
    return "file deleted, fileName: " + fileName;
  }

  public String getDocumentName(String fileName) {
    String[] s3KeyName = fileName.split("/");
    return s3KeyName[s3KeyName.length - 1];
  }
}
