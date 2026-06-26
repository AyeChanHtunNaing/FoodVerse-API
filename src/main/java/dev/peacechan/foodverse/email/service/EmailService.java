package dev.peacechan.foodverse.email.service;

import dev.peacechan.foodverse.entity.Order;
import dev.peacechan.foodverse.enums.OrderStatus;
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
    public void sendOrderCreatedEmail(Order order) {
        sendEmail(
                order.getCustomerProfile().getUser().getEmail(),
                "Order Created - " + order.getOrderNumber(),
                buildOrderCreatedBody(order)
        );
    }

    @Async("mailTaskExecutor")
    public void sendOrderUpdatedEmail(Order order) {
        sendEmail(
                order.getCustomerProfile().getUser().getEmail(),
                "Order Updated - " + order.getOrderNumber(),
                buildOrderUpdatedBody(order)
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

    private String buildOrderCreatedBody(Order order) {
        return """
                Hello %s,

                Your order has been created successfully.

                Order Number: %s
                Status: %s
                Total Amount: %s
                Restaurant ID: %d

                Thank you for choosing Foodverse.
                """.formatted(
                order.getCustomerProfile().getUser().getFullName(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getRestaurant().getId()
        );
    }

    private String buildOrderUpdatedBody(Order order) {
        return """
                Hello %s,

                Your order status has been updated.

                Order Number: %s
                New Status: %s
                Total Amount: %s

                Thank you for choosing Foodverse.
                """.formatted(
                order.getCustomerProfile().getUser().getFullName(),
                order.getOrderNumber(),
                formatStatus(order.getStatus()),
                order.getTotalAmount()
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
