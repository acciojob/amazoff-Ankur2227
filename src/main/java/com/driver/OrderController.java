package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add-order")
    public ResponseEntity<Void> addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<Void> addPartner(@PathVariable String partnerId) {
        orderService.addPartner(partnerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<Void> addOrderPartnerPair(
            @RequestParam String orderId,
            @RequestParam String partnerId) {
        orderService.assignOrderToPartner(orderId, partnerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return (order != null)
                ? ResponseEntity.ok(order)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) {
        DeliveryPartner partner = orderService.getPartnerById(partnerId);
        return (partner != null)
                ? ResponseEntity.ok(partner)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) {
        return ResponseEntity.ok(orderService.getOrderCountByPartnerId(partnerId));
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) {
        return ResponseEntity.ok(orderService.getOrdersByPartnerId(partnerId));
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders() {
        return ResponseEntity.ok(orderService.getCountOfUnassignedOrders());
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{time}/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(
            @PathVariable String time,
            @PathVariable String partnerId) {
        return ResponseEntity.ok(
            orderService.getCountOfOrdersLeftAfterGivenTime(time, partnerId)
        );
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) {
        return ResponseEntity.ok(orderService.getLastDeliveryTimeByPartnerId(partnerId));
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<Void> deletePartnerById(@PathVariable String partnerId) {
        orderService.deletePartnerById(partnerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable String orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }
}
