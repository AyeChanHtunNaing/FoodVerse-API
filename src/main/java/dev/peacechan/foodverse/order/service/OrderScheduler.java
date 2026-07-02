package dev.peacechan.foodverse.order.service;

import dev.peacechan.foodverse.order.entity.Order;
import dev.peacechan.foodverse.order.enums.OrderStatus;
import dev.peacechan.foodverse.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredPreparingOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);
        List<Order> expiredOrders = orderRepository.findAllByStatusAndOrderedAtBefore(OrderStatus.PREPARING, cutoff);

        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.CANCELLED);
        }
    }
}
