package com.deploy.service.utils;

public interface SNSService {
    void sendMessage(String webhookUrl, String message);
}
