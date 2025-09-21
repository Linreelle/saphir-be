package com.linreelle.saphir.service;

import com.linreelle.saphir.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linreelle.saphir.utils.EmailUtils.adhesionMail;
import static com.linreelle.saphir.utils.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

//    @Value("${spring.mail.verify.host}")
//    private String host;
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//    private final JavaMailSender mailSender;
//
//    public void sendSimpleMailMessage(String name, String to, String token) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setSubject("New User Account Verification");
//            message.setFrom(fromEmail);
//            message.setTo(to);
//            message.setText(getEmailMessage(name, host, token));
//            mailSender.send(message);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new RuntimeException(exception.getMessage());
//        }
//    }
//
//    public void sendAdhesionEmail(String name, String to, String selectedPackageInfo) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setSubject("Adhesion Request");
//            message.setFrom(fromEmail);
//            message.setTo(to);
//            message.setText(adhesionMail(name));
//            mailSender.send(message);
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new RuntimeException(exception.getMessage());
//        }
//    }


    @Value("${mailtrap.api.token}")
    private String apiToken;

    @Value("${email.from}")
    private String fromEmail;

    private final String MAILTRAP_SEND_URL = "https://send.api.mailtrap.io/api/send";

    public void sendSimpleMailMessage(String name, String to, String token) {
        String subject = "New User Account Verification";
        String text = EmailUtils.getEmailMessage(name, "https://linreelle.github.io/saphir-fe/", token);
        sendEmail(to, subject, text);
    }

    public void sendAdhesionEmail(String name, String to, String emailBody) {
        String subject = "Adhesion Request";
        sendEmail(to, subject, emailBody);
    }

    private void sendEmail(String to, String subject, String text) {
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("Mailtrap API Token: " + apiToken);

        Map<String, Object> body = new HashMap<>();
        body.put("from", Map.of("email", fromEmail));
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", subject);
        body.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(MAILTRAP_SEND_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }

}

