package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final S3Validator s3Validator;
    private final Validator validator;
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //프로필 이미지 수정하기
    @Transactional
    public String updateImage(UserDetailsImpl userDetails, MultipartFile multipartFile) {
        User user = validator.userInfo(userDetails);

        if (user.getUserImage() != null) {
            String deleteUrl = user.getUserImage().replace("https://myawsssam2.s3.ap-northeast-2.amazonaws.com/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);
                String ImageUrl = s3Validator.uploadOne(multipartFile);

                user.setUserImage(ImageUrl);
                userRepository.save(user);
            }
        } else {
            String ImageUrl = s3Validator.uploadOne(multipartFile);

            user.setUserImage(ImageUrl);
            userRepository.save(user);
        }

        return "업로드 성공";
    }


    //1. 회원가입 시, 디폴트 이미지 넣어줄 것
    //2. 이미지 수정 시, null 값으로 보내면 디폴트 이미지 넣어줄 것
    //3. respones Entity 로 return 하기
}

