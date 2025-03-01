package ru.otus.hw.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifyWhenCanceledHandler {

    private final OrderRepository orderRepository;

    public MailMessage handle(OrderCanceledEvent event) {
        var order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new OrderNotFoundException(event.orderId()));

        var mail = new SimpleMailMessage();
        mail.setTo(order.email());
        mail.setSubject("Your order has been canceled");
        mail.setText("Unfortunately, your order has been canceled due to insufficient supply " +
                     "and will be refunded in 1 week");

        return mail;
    }
}
