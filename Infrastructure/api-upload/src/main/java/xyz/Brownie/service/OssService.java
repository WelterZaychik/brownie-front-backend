package xyz.Brownie.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String OssUpload(MultipartFile file);
}
