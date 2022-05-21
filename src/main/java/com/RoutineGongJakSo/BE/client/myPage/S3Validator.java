package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.exception.CustomException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Validator {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    // 이미지파일명 중복 방지
    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName)); // 확장자와 파일 이름을 붙여줌
    }

    //하나의 파일 업로드
    public String uploadOne(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        String imageUrl = "";
        ObjectMetadata objectMetadata = new ObjectMetadata(); // ObjectMetadata Amazon S3와 함께 저장된 객체 메타데이터
        objectMetadata.setContentLength(file.getSize()); //연결된 개체의 크기를 바이트 단위로 나타내는 Content-Length HTTP 헤더를 설정
        objectMetadata.setContentType(file.getContentType()); //연결된 개체에 저장된 콘텐츠 유형을 나타내는 Content-Type HTTP 헤더를 설정

        //Stream = 데이터가 전송되는 통로
        //IntputStream 은 데이터를 byte 단위로 읽어들이는 통로
        // 읽어들인 데이터를 byte로 돌려줌
        try(InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket,fileName,inputStream,objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imageUrl = s3Client.getUrl(bucket, fileName).toString();
        }catch (IOException e){
            throw new CustomException(FAIL_FILE_UPLOAD);
        }
        return imageUrl;
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new CustomException(BAD_FORM_TYPE);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf(".")); //마지막 "." 을 찾음
        if (!fileValidate.contains(idxFileName)) { // 해당 문자열이 포함되어 있는지 확인
            throw new CustomException(BAD_FORM_TYPE);
        }
        return idxFileName;
    }

    // file 업로드
    public String uploadTxtFile(File file, int cnt) {
        String fileName = file.getName();
        String onlyFileName = fileName.split("\\.")[0];
        String saveName = onlyFileName+"_"+cnt+".txt";

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket+"/chat",saveName,file);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client().putObject(putObjectRequest);
        return saveName;
    }

    public S3Object getTxtFile(String fileName){
        GetObjectRequest getObjectRequest =
                new GetObjectRequest(bucket+"/chat", fileName);
        S3Object targetFile = amazonS3Client().getObject(getObjectRequest);
        return targetFile;
    }
}
