package com.driver;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final HashMap<String, Order> orderMap;
    private final HashMap<String, DeliveryPartner> partnerMap;
    private final HashMap<String, HashSet<String>> partnerToOrderMap;
    private final HashMap<String, String> orderToPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.partnerToOrderMap = new HashMap<>();
        this.orderToPartnerMap = new HashMap<>();
    }

    public void saveOrder(Order order) {
        orderMap.put(order.getOrderId(), order);
    }

    public void savePartner(String partnerId) {
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        partnerToOrderMap.put(partnerId, new HashSet<>());
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            // assign partner to order
            orderToPartnerMap.put(orderId, partnerId);

            // add order to partner's list
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            orders.add(orderId);

            // increment partner's order count
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
        }
    }

    public Order findOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        return partnerToOrderMap.getOrDefault(partnerId, new HashSet<>()).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId, new HashSet<>()));
    }

    public List<String> findAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId) {
        // unassign all orders of this partner
        HashSet<String> orders = partnerToOrderMap.remove(partnerId);
        if (orders != null) {
            for (String orderId : orders) {
                orderToPartnerMap.remove(orderId);
            }
        }
        // remove the partner
        partnerMap.remove(partnerId);
    }

    public void deleteOrder(String orderId) {
        // remove from order->partner mapping
        String pid = orderToPartnerMap.remove(orderId);
        if (pid != null) {
            // remove from partner's set and decrement count
            HashSet<String> orders = partnerToOrderMap.get(pid);
            if (orders != null) {
                orders.remove(orderId);
                DeliveryPartner partner = partnerMap.get(pid);
                partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);
            }
        }
        // remove the order itself
        orderMap.remove(orderId);
    }

    public Integer findCountOfUnassignedOrders() {
        int count = 0;
        for (String orderId : orderMap.keySet()) {
            if (!orderToPartnerMap.containsKey(orderId)) {
                count++;
            }
        }
        return count;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        int cutoff = convertTimeToMinutes(timeString);
        int count = 0;
        for (String orderId : partnerToOrderMap.getOrDefault(partnerId, new HashSet<>())) {
            Order o = orderMap.get(orderId);
            if (o != null && convertTimeToMinutes(o.getDeliveryTime()) > cutoff) {
                count++;
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        int max = 0;
        for (String orderId : partnerToOrderMap.getOrDefault(partnerId, new HashSet<>())) {
            Order o = orderMap.get(orderId);
            if (o != null) {
                max = Math.max(max, convertTimeToMinutes(o.getDeliveryTime()));
            }
        }
        int hh = max / 60;
        int mm = max % 60;
        return String.format("%02d:%02d", hh, mm);
    }

    // helper to convert "HH:MM" → total minutes
    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h * 60 + m;
    }
}
