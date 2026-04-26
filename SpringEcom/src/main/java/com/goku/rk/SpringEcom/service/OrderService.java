package com.goku.rk.SpringEcom.service;

import com.goku.rk.SpringEcom.model.Order;
import com.goku.rk.SpringEcom.model.OrderItem;
import com.goku.rk.SpringEcom.model.Product;
import com.goku.rk.SpringEcom.model.dto.OrderItemRequest;
import com.goku.rk.SpringEcom.model.dto.OrderItemResponse;
import com.goku.rk.SpringEcom.model.dto.OrderRequest;
import com.goku.rk.SpringEcom.model.dto.OrderResponse;
import com.goku.rk.SpringEcom.repo.OrderRepo;
import com.goku.rk.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo prepo;
    private OrderRepo orepo;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String oId = "ORD" + (UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderId(oId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());
        List<OrderItem> orderItem = new ArrayList<>();
        for (OrderItemRequest orq : orderRequest.items()) {
            Product prod = prepo.findById(orq.productId()).orElseThrow(() -> new RuntimeException("Product not found"));
            prod.setStockQuantity(prod.getStockQuantity() - orq.quantity());
            prepo.save(prod);

            OrderItem orderI = OrderItem.builder().product(prod).quantity(orq.quantity()).totalPrice(prod.getPrice().multiply(BigDecimal.valueOf(orq.quantity()))).order(order).build();
            orderItem.add(orderI);
        }
        order.setOrderItems(orderItem);
        Order saveOrder = orepo.save(order);

        List<OrderItemResponse> orderItemResponse = new ArrayList<>();
        for (OrderItem oi : orderItem) {
            OrderItemResponse oires = new OrderItemResponse(oi.getProduct().getName(), oi.getQuantity(), oi.getTotalPrice());
            orderItemResponse.add(oires);
        }

        OrderResponse orderResponse = new OrderResponse(saveOrder.getOrderId(), saveOrder.getCustomerName(), saveOrder.getEmail(), saveOrder.getStatus(), LocalDate.now(), orderItemResponse);
        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponse() {
        List<Order> orders = orepo.findAll();
        List<OrderResponse> orderRes = new ArrayList<>();
        for (Order o : orders)
        {
            List<OrderItemResponse> orderItemrRes = new ArrayList<>();
            for (OrderItem oi : o.getOrderItems()) {
                OrderItemResponse oir = new OrderItemResponse(oi.getProduct().getName(), oi.getQuantity(), oi.getTotalPrice());
                orderItemrRes.add(oir);
            }
            OrderResponse ors = new OrderResponse(o.getOrderId(), o.getCustomerName(), o.getEmail(), o.getStatus(), o.getOrderDate(), orderItemrRes);
            orderRes.add(ors);
        }
        return orderRes;

    }
}
