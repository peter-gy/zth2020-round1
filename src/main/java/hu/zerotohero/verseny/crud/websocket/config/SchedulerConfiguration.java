package hu.zerotohero.verseny.crud.websocket.config;

import hu.zerotohero.verseny.crud.websocket.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 1500)
    public void sendMessages() {
        simpMessagingTemplate.convertAndSend("/topic/order", new OrderResponse("Order response"));
    }

}
