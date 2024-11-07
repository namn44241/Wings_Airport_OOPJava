package com.example.quanlisanbay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String language, String fullName) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("wingsairport73@gmail.com");
        helper.setTo(to);

        String subject;
        String htmlBody;

        if ("tieng-viet".equals(language)) {
            subject = "Xác nhận đăng ký Wings Airport";
            htmlBody = buildVietnameseEmailBody(fullName);
        } else {
            subject = "Wings Airport Subscription Confirmation";
            htmlBody = buildEnglishEmailBody(fullName);
        }

        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    private String buildVietnameseEmailBody(String fullName) {
        return "<html><body>" +
               "<h1>Xin chào " + fullName + ",</h1>" +
               "<h3>Đây là Wings Airport, chúc bạn một ngày tốt lành!</h3>" +
               "<img width=\"480\" height=\"269\" src=\"https://media.giphy.com/media/S2IfEQqgWc0AH4r6Al/giphy.gif\" alt=\"hello\">" +
               "<p>Wings Airport tự hào là Sân bay hàng không quốc tế 4 sao.<br>" +
               "Xin trân trọng cảm ơn sự đồng hành của Quý khách và bạn hàng!</p>" +
               "<p>Trân trọng,<br>Đội ngũ Wings Airport</p>" +
               "</body></html>";
    }

    private String buildEnglishEmailBody(String fullName) {
        return "<html><body>" +
               "<h1>Hello " + fullName + ",</h1>" +
               "<h3>This is Wings Airport, have a great day!</h3>" +
               "<img width=\"480\" height=\"269\" src=\"https://media.giphy.com/media/S2IfEQqgWc0AH4r6Al/giphy.gif\" alt=\"hello\">" +
               "<p>Wings Airport is proud to be a 4-star non-international airport.<br>" +
               "We sincerely thank our valued customers and partners for their support!</p>" +
               "<p>Best regards,<br>Wings Airport Team</p>" +
               "</body></html>";
    }
}