package com.driver;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        orderRepository.saveOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.savePartner(partnerId);
    }

    public void assignOrderToPartner(String orderId, String partnerId) {
        orderRepository.saveOrderPartnerMap(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.findPartnerById(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return orderRepository.findOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.findOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepository.findAllOrders();
    }

    public int getCountOfUnassignedOrders() {
        return orderRepository.findCountOfUnassignedOrders();
    }

    public int getCountOfOrdersLeftAfterGivenTime(String time, String partnerId) {
        return orderRepository.findOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return orderRepository.findLastDeliveryTimeByPartnerId(partnerId);
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartner(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrder(orderId);
    }
}