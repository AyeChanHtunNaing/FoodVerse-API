package dev.peacechan.foodverse.email.service;

import dev.peacechan.foodverse.enums.OrderStatus;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:${spring.mail.username:no-reply@foodverse.local}}")
    private String fromEmail;

    @Async("mailTaskExecutor")
    public void sendOrderCreatedEmail(
            String to,
            String fullName,
            String orderNumber,
            OrderStatus status,
            BigDecimal totalAmount,
            Long restaurantId
    ) {
        sendEmail(
                to,
                "Order Created - " + orderNumber,
                buildOrderCreatedBody(fullName, orderNumber, status, totalAmount, restaurantId)
        );
    }

    @Async("mailTaskExecutor")
    public void sendOrderUpdatedEmail(
            String to,
            String fullName,
            String orderNumber,
            OrderStatus status,
            BigDecimal totalAmount
    ) {
        sendEmail(
                to,
                "Order Updated - " + orderNumber,
                buildOrderUpdatedBody(fullName, orderNumber, status, totalAmount)
        );
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Failed to send email to {}", to, ex);
        }
    }

    private String buildOrderCreatedBody(
            String fullName,
            String orderNumber,
            OrderStatus status,
            BigDecimal totalAmount,
            Long restaurantId
    ) {
        return """
                Hello %s,

                Your order has been created successfully.

                Order Number: %s
                Status: %s
                Total Amount: %s
                Restaurant ID: %d

                Thank you for choosing Foodverse.
                """.formatted(
                fullName,
                orderNumber,
                status.name(),
                totalAmount,
                restaurantId
        );
    }

    private String buildOrderUpdatedBody(
            String fullName,
            String orderNumber,
            OrderStatus status,
            BigDecimal totalAmount
    ) {
        return """
                Hello %s,

                Your order status has been updated.

                Order Number: %s
                New Status: %s
                Total Amount: %s

                Thank you for choosing Foodverse.
                """.formatted(
                fullName,
                orderNumber,
                formatStatus(status),
                totalAmount
        );
    }

    private String formatStatus(OrderStatus status) {
        return switch (status) {
            case PLACED -> "PLACED";
            case PREPARING -> "PREPARING";
            case DELIVERED -> "DELIVERED";
            case CANCELLED -> "CANCELLED";
        };
    }
}
