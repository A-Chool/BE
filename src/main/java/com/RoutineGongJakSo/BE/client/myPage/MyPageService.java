package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.client.tag.Tag;
import com.RoutineGongJakSo.BE.client.tag.TagRepository;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.LIAR_USER_IMAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
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

        // AWS에 요청할 Client를 생성해 주는 역할
        // Builder 권장
        // 해당 객체의 standard()를 통해 AmazonS3 Client 를 생성하며, credentials 와 region 정보를 넘겨줌
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //프로필 이미지 수정하기
    @Transactional
    public String updateImage(UserDetailsImpl userDetails, MultipartFile multipartFile) {
        User user = validator.userInfo(userDetails);

        if (user.getUserImageUrl() != null) {
            String deleteUrl = user.getUserImageUrl().replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/userProfile/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            String imageUrl = "";
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);

                imageUrl = s3Validator.uploadOne(multipartFile);

                user.setUserImageUrl(imageUrl);
                userRepository.save(user);
            }

            log.info("수정된 이미지 {}", imageUrl);

            return imageUrl;
        } else {
            String imageUrl = s3Validator.uploadOne(multipartFile);

            user.setUserImageUrl(imageUrl);
            userRepository.save(user);

            log.info("수정된 이미지 {}", imageUrl);

            return imageUrl;
        }
    }

    // 프로필 이미지 삭제
    @Transactional
    public String deleteImage(UserDetailsImpl userDetails) {
        User user = validator.userInfo(userDetails);
        if (user.getUserImageUrl() != null) {
            String deleteUrl = user.getUserImageUrl().replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/userProfile/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);
                user.setUserImageUrl(null);
                userRepository.save(user);
                log.info("삭제된 이미지 {}", deleteUrl);
            }
        } else {
            throw new CustomException(LIAR_USER_IMAGE);
        }
        return "프로필 이미지 삭제 완료!";
    }

    //프로필 정보 업데이트
    @Transactional
    public MyPageDto.ResponseDto updateUserInfo(UserDetailsImpl userDetails, MyPageDto.PutRequestDto myPageDto) {
        User user = validator.userInfo(userDetails);

        List<Tag> tagList = tagRepository.findByUser(userDetails.getUser());

        if (myPageDto.getUserTag() != null){
            tagRepository.deleteAll(tagList);
            if (myPageDto.getUserTag().contains(",")){
                String[] arrTag = myPageDto.getUserTag().split(",");
                for (String t : arrTag){
                    Tag saveTag = new Tag(t.trim(), user);
                    tagRepository.save(saveTag);
                }
            } else {
                Tag saveTag = new Tag(myPageDto.getUserTag(), user);
                tagRepository.save(saveTag);
            }
        } else {
            Tag saveTag = new Tag(myPageDto.getUserTag(), user);
            tagRepository.save(saveTag);
        }







        user.setUserName(myPageDto.getUserName());
        user.setUserGitHub(myPageDto.getUserGitHub());
        user.setFindKakaoId(myPageDto.getFindKakaoId());
        user.setPhoneNumber(myPageDto.getPhoneNumber());

        userRepository.save(user);

        log.info("업데이트된 정보 {}", getMyPage(userDetails));

        return getMyPage(userDetails);
    }

    // 마이페이지 조회
    public MyPageDto.ResponseDto getMyPage(UserDetailsImpl userDetails) {
        User user = validator.userInfo(userDetails);

        String userImage = user.getUserImageUrl();

        if (user.getUserImageUrl() == null){
            userImage = "https://i.esdrop.com/d/f/zoDvw3Gypq/575gyh5UjD.png";
        }

        List<Tag> tags = tagRepository.findByUser(userDetails.getUser());
        List<String> findTags = new ArrayList<>();

        for (Tag tag : tags) {
            findTags.add(tag.getTag());
        }

        MyPageDto.ResponseDto responseDto = MyPageDto.ResponseDto.builder()
                .userId(user.getUserId())
                .kakaoId(user.getKakaoId())
                .findKakaoId(user.getFindKakaoId())
                .userEmail(user.getUserEmail())
                .userImage(userImage)
                .userPhoneNumber(user.getPhoneNumber())
                .username(user.getUserName())
                .userTag(findTags)
                .userGitHub(user.getUserGitHub())
                .build();

        log.info("마이페이지 조회 {}", responseDto);

        return responseDto;
    }
}

