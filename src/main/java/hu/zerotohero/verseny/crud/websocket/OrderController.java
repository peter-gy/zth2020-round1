package hu.zerotohero.verseny.crud.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final long WAIT = 1000;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/order-new")
    @SendTo("/topic/completed-orders")
    public String handleOrder(String order) throws InterruptedException {
        Thread.sleep(WAIT);

        String msgFromWaiterToCook = String.format("Hey chef, please start to work on this order: %s!", order);
        simpMessagingTemplate.convertAndSend("/topic/registered-orders", msgFromWaiterToCook);

        String msgFromCookToWaiter = String.format("Hey waiter, I have completed this order: %s!", order);
        simpMessagingTemplate.convertAndSend("/topic/prepared-orders", msgFromCookToWaiter);

        return "Dear customer, your " + order + " is waiting for you!";
    }

}
