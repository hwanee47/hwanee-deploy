package com.deploy.service.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SlackService implements SNSService {

    @Override
    public void sendMessage(String webhookUrl, String message) {

        if (!StringUtils.hasText(webhookUrl))
            throw new IllegalArgumentException("webhookUrl은 필수값입니다.");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setText(message);

        HttpEntity<SlackMessage> entity = new HttpEntity<>(slackMessage, headers);

        restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
    }


    @Getter
    @Setter
    public static class SlackMessage {
        private String text;
    }
}
