package hu.zerotohero.verseny.crud.websocket;

import hu.zerotohero.verseny.crud.websocket.model.Order;
import hu.zerotohero.verseny.crud.websocket.model.OrderResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    @MessageMapping("/order")
    @SendTo("/topic/order")
    public OrderResponse send(Order order) {
        return new OrderResponse("Processing " + order.getRequest() + "...");
    }

}
