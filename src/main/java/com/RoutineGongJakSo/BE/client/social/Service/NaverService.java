package com.RoutineGongJakSo.BE.client.social.Service;

import com.RoutineGongJakSo.BE.client.refreshToken.RefreshToken;
import com.RoutineGongJakSo.BE.client.refreshToken.RefreshTokenRepository;
import com.RoutineGongJakSo.BE.client.social.Dto.NaverUserInfoDto;
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
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${naver.client_id}")
    String naverClientId;

    @Value("${naver.client_secret}")
    String naverSecret;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SlackAlert slackAlert;

    // ????????? ?????????
    public void naverLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "????????????" ??? "????????? ??????" ??????
        String accessToken = getAccessToken(code);

        // 2. ???????????? ????????? API ??????
        NaverUserInfoDto naverUserInfoDto = getNaverUserInfo(accessToken);

        // 3. ?????????ID??? ???????????? ??????
        User NaverUser = signupNaverUser(naverUserInfoDto);

        // 4. ?????? ????????? ??????
        Authentication authentication = forceLoginNaverUser(NaverUser);

        // 5. response Header??? JWT ?????? ??????
        naverUsersAuthorizationInput(authentication, response);
    }


    //header ??? Content-type ??????
    //1???
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        final String state = new BigInteger(130, new SecureRandom()).toString();
        System.out.println("getCode : " + code);

        //HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverClientId);
        body.add("client_secret", naverSecret);
        body.add("redirect_uri", "https://www.a-chool.com/api/user/naver/callback");
        body.add("code", code);
        body.add("state", state);

        //HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        //HTTP ?????? (JSON) -> ????????? ?????? ??????
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        log.info("????????? ?????? ??????{}", jsonNode);
//        log.info("??????????????? ????????? ?????? ?????? {}", jsonNode.get("access_token").asText());
        return jsonNode.get("access_token").asText();
    }

    //2???
    private NaverUserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        final String state = new BigInteger(130, new SecureRandom()).toString();

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response")
                .get("nickname").asText();
        String email = jsonNode.get("response")
                .get("email").asText();

        log.info("????????? ????????? ?????? id: {},{},{}", id, nickname, email);

        return new NaverUserInfoDto(id, nickname, email);
    }

    // 3???
    private User signupNaverUser(NaverUserInfoDto naverUserInfoDto) {
        // DB ??? ????????? Naver Id ??? ????????? ??????
        String naverId = naverUserInfoDto.getNaverId();
        User findNaver = repository.findByNaverId(naverId)
                .orElse(null);

        if (findNaver == null) {
            //????????????
            //username = naverNickname
            String nickName = naverUserInfoDto.getUserName();

            //password : random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            // email : naver email
            String email = naverUserInfoDto.getEmail();

            User naverUser = User.builder()
                    .userName(nickName)
                    .userEmail(email)
                    .naverId(naverId)
                    .userPw(encodedPassword)
                    .userLevel(0)
                    .build();

            log.info("????????? ???????????? ???????????? {}", naverUser);

            repository.save(naverUser);

            slackAlert.joinAlert(naverUser);

            return naverUser;

        }
        log.info("????????? ???????????? ?????? ?????? {}", findNaver);
        return findNaver;
    }

    // 4???
    private Authentication forceLoginNaverUser(User naverUser) {
        UserDetails userDetails = new UserDetailsImpl(naverUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("?????? ????????? {}", authentication);
        return authentication;
    }

    // 5???
    private void naverUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
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

