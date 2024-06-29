package com.wanchcoach.alarm.domain.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.wanchcoach.alarm.domain.repository.AlarmQRepository;
import com.wanchcoach.alarm.domain.service.dto.DrugAlarm;
import com.wanchcoach.alarm.domain.service.dto.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    @Value("${fcm.secret}")
    private String FCM_KEY;

    @Value("${fcm.api-url}")
    private String API_URL;
    private final ObjectMapper objectMapper;

    private final AlarmQRepository alarmQRepository;

    @Scheduled(cron = "0 0/10 * * * *")
    @Scheduled(cron = "0/10 * * * * *")
    public void drugAlarmCheck() throws IOException {
        log.info("10분마다 시간 조회 실시 -> " + LocalTime.now());
//        List<DrugAlarm> alarms = alarmQRepository.getNeedDrugAlarm(LocalTime.now());
        String msg = "~~님 약먹을 시간이에요.";
        sendMessageTo("cDvxywwvRmn-wIjkLSTKiz:APA91bFJr3Dtp1tpa7wOIzUKvV3akZb6pbBJ8vfUMG8OdrAgN7whFrQt09UDFji9KmQ1LxpAbFC5gWzruccFJLBiyQB60S5DcuKPKoKVz5e2okIUku3L40-EE902-sH54YXEVfupYFnA",
                "현재 시간",
                LocalDateTime.now().toString());
    }
    @Scheduled(cron = "0 0 8 * * *")
    public void todayTreatmentCheck() {
        log.info("당일 예약 조회 -> " + LocalTime.now());
        List<DrugAlarm> alarms = alarmQRepository.getTodayTreatment(LocalDateTime.now());
        String msg = "~~님 오늘 진료 예약이 있어요.";
    }
    @Scheduled(cron = "0 0 19 * * *")
    public void tomorrowTreatmentCheck() {
        log.info("다음날 진료 예약 조회 -> " + LocalTime.now());
        List<DrugAlarm> alarms = alarmQRepository.getTomorrowTreatment(LocalDateTime.now());
        String msg = "~~님 내일 진료 예약이 있어요.";
    }

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(FCM_KEY.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(serviceAccountStream)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
