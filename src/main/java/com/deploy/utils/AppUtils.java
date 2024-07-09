package com.deploy.utils;

public class AppUtils {

    /**
     * Milliseconds를 시분초 텍스트로 변경
     * ex) 185000L -> 3m 5s
     * @param millis
     * @return
     */
    public static String convertToRunTime(Long millis) {

        if (millis == null)
            return null;

        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;

        StringBuilder timeString = new StringBuilder();
        if (hours > 0) {
            timeString.append(hours).append("h ");
        }
        if (minutes > 0) {
            timeString.append(minutes).append("m ");
        }
        if (seconds > 0) {
            timeString.append(seconds).append("s");
        }

        return timeString.toString().trim();
    }
}
