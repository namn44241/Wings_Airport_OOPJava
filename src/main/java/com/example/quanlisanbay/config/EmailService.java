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
        return "<html><body style=\"font-family: Arial, sans-serif; color: #333;\">" +
                "<div style=\"max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;\">"
                +
                "<h1 style=\"color: #4CAF50;\">Xin chào " + fullName + ",</h1>" +
                "<p style=\"font-size: 16px;\">Đây là <strong>Wings Airport</strong>, chúc bạn một ngày tốt lành!</p>" +
                "<div style=\"text-align: center;\">" +
                "<img width=\"480\" height=\"269\" src=\"https://media.giphy.com/media/S2IfEQqgWc0AH4r6Al/giphy.gif\" alt=\"hello\" style=\"border-radius: 10px;\">"
                +
                "</div>" +
                "<p style=\"font-size: 16px; line-height: 1.5;\">Wings Airport tự hào là Sân bay hàng không quốc tế 4 sao.<br>"
                +
                "Xin trân trọng cảm ơn sự đồng hành của Quý khách và bạn hàng!</p>" +
                "<p style=\"font-size: 16px;\">Trân trọng,<br><strong>Đội ngũ Wings Airport</strong></p>" +
                "<hr style=\"border: 0; height: 1px; background-color: #ddd;\">" +
                "<p style=\"font-size: 12px; color: #888;\">Lưu ý: Đây là email tự động. Vui lòng không trả lời email này.</p>"
                +
                "</div>" +
                "</body></html>";
    }

    private String buildEnglishEmailBody(String fullName) {
        return "<html><body style=\"font-family: Arial, sans-serif; color: #333;\">" +
                "<div style=\"max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;\">"
                +
                "<h1 style=\"color: #4CAF50;\">Hello " + fullName + ",</h1>" +
                "<p style=\"font-size: 16px;\">This is <strong>Wings Airport</strong>, have a great day!</p>" +
                "<div style=\"text-align: center;\">" +
                "<img width=\"480\" height=\"269\" src=\"https://media.giphy.com/media/S2IfEQqgWc0AH4r6Al/giphy.gif\" alt=\"hello\" style=\"border-radius: 10px;\">"
                +
                "</div>" +
                "<p style=\"font-size: 16px; line-height: 1.5;\">Wings Airport is proud to be a 4-star international airport.<br>"
                +
                "We sincerely thank our valued customers and partners for their support!</p>" +
                "<p style=\"font-size: 16px;\">Best regards,<br><strong>Wings Airport Team</strong></p>" +
                "<hr style=\"border: 0; height: 1px; background-color: #ddd;\">" +
                "<p style=\"font-size: 12px; color: #888;\">Note: This is an automated email. Please do not reply to this email.</p>"
                +
                "</div>" +
                "</body></html>";
    }

}