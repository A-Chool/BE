package com.RoutineGongJakSo.BE.client.social.Service;

import com.RoutineGongJakSo.BE.client.refreshToken.RefreshToken;
import com.RoutineGongJakSo.BE.client.refreshToken.RefreshTokenRepository;
import com.RoutineGongJakSo.BE.client.social.Dto.KakaoUserInfoDto;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import com.RoutineGongJakSo.BE.util.SlackAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client_id}")
    String kakaoClientId;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SlackAlert slackAlert;

    public KakaoUserInfoDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "????????????" ??? "????????? ??????" ??????
        String accessToken = getAccessToken(code);

        // 2. ???????????? ????????? API ??????
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. ?????????ID??? ???????????? ??????
        User kakaoUser = signupKakaoUser(kakaoUserInfo);

        // 4. ?????? ????????? ??????
        Authentication authentication = forceLoginKakaoUser(kakaoUser);

        // 5. response Header??? JWT ?????? ??????
        kakaoUsersAuthorizationInput(authentication, response);
        return kakaoUserInfo;
    }


    //header ??? Content-type ??????
    //1???
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("getCode : " + code);

        //HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", "https://www.a-chool.com/api/user/kakao/callback");
        body.add("code", code);

        //HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //HTTP ?????? (JSON) -> ????????? ?????? ??????
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info("??????????????? ????????? ?????? ?????? {}", jsonNode.get("access_token").asText());
        return jsonNode.get("access_token").asText();
    }

    //2???
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("????????? ????????? ?????? id: {},{},{}", id, nickname, email);

        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3???
    private User signupKakaoUser(KakaoUserInfoDto kakaoUserInfoDto) {
        // DB ??? ????????? Kakao Id ??? ????????? ??????
        Long kakaoId = kakaoUserInfoDto.getKakaoId();
        User findKakao = repository.findByKakaoId(kakaoId)
                .orElse(null);

        if (findKakao == null) {
            //????????????
            //username = kakaoNickname
            String nickName = kakaoUserInfoDto.getUserName();

            //password : random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            // email : kakao email
            String email = kakaoUserInfoDto.getEmail();

            User kakaoUser = User.builder()
                    .userName(nickName)
                    .userEmail(email)
                    .kakaoId(kakaoId)
                    .userPw(encodedPassword)
                    .userLevel(0)
                    .build();

            repository.save(kakaoUser);
            log.info("????????? ???????????? ???????????? {}", kakaoUser);

            slackAlert.joinAlert(kakaoUser);

            return kakaoUser;
        }
        log.info("????????? ???????????? ?????? ?????? {}", findKakao);
        return findKakao;
    }

    // 4???
    private Authentication forceLoginKakaoUser(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("?????? ????????? {}", authentication);
        return authentication;
    }

    // 5???
    private void kakaoUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
        // response header??? token ??????
        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
        String refreshToken = JwtTokenUtils.generateRefreshToken();

        response.addHeader("Authorization", "BEARER" + " " + token);
        response.addHeader("RefreshAuthorization", "BEARER" + " " + refreshToken);

        log.info("????????? ?????? {}", token);
        log.info("???????????? ?????? {} ", refreshToken);

        RefreshToken findToken = refreshTokenRepository.findByUserEmail(userDetailsImpl.getUserEmail());

        if (findToken != null) {
            findToken.setRefreshToken(JwtTokenUtils.generateRefreshToken());
            log.info("???????????? ?????? ?????? {}", findToken);
            return;
        }

        //???????????? ????????? ??????
        RefreshToken refresh = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userEmail(userDetailsImpl.getUserEmail())
                .build();

        log.info("???????????? ?????? ?????? {}", refresh);
        refreshTokenRepository.save(refresh);
    }
}