package com.sparta.ssaktium.domain.common.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    // S3
    public final AmazonS3Client s3Client;

    // 이미지 파일 이름 변경 메서드
    private String changeFileName(String originalFileName) {
        // 이미지 등록 날짜를 붙여서 리턴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter) + "_" + originalFileName;
    }

    // 이미지 등록 후 URL 호출 메서드
    public String uploadImageToS3(MultipartFile image, String bucket) throws IOException {
        // 이미지 이름 변경
        String originalFileName = image.getOriginalFilename();
        String fileName = changeFileName(originalFileName);

        // S3에 파일을 보낼 때 파일의 종류와 크기를 알려주기
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        metadata.setContentDisposition("inline");

        // S3에 파일 업로드
        s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

        return s3Client.getUrl(bucket, fileName).toString();
    }

    // 등록된 사진 기존 URL 원본 파일이름으로 디코딩
    public String extractFileNameFromUrl(String url) {
        try {
            // URL 마지막 슬래시의 위치를 찾아서 인코딩된 파일 이름 가져오기
            String encodedFileName = url.substring(url.lastIndexOf("/") + 1);

            // 인코딩된 파일 이름을 디코딩 해서 진짜 원본 파일 이름 가져오기
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("원본 파일 이름 변경 에러", e);
        }
    }

    //여러 이미지 업로드
    public List<String> uploadImageListToS3(List<MultipartFile> images,String bucket) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageUrl = uploadImageToS3(image, bucket);
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    //이미지 리스트 원본파일이름으로 디코딩
    public List<String> extractFileNamesFromUrls(List<String> urls) {
        List<String> fileNames = new ArrayList<>();

        for (String url : urls) {
            String fileName = extractFileNameFromUrl(url); // 복호화
            fileNames.add(fileName);
        }

        return fileNames;
    }
}
