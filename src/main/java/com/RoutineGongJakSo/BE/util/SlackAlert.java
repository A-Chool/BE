package com.RoutineGongJakSo.BE.util;

import com.RoutineGongJakSo.BE.client.user.User;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SlackAlert {

    @Value("${logging.slack.webhook-uri}")
    private String slackUrl;

    public void joinAlert(User user) {
        log.info("joinAlert");
        log.info("slackUrl : {}", slackUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", user.getUserName() + " 신규회원 가입 " + new DateTime());
        String body = jsonObject.toString();

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(slackUrl, HttpMethod.POST, requestEntity, String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
    }
}
