package com.example.order_service.service;

import com.example.order_service.client.ProductServiceClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.exception.DatabaseSaveException;
import com.example.order_service.exception.InvalidOrderException;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    @Transactional
    public Order createOrder(OrderRequest request) {
        if (request == null) {
            throw new InvalidOrderException("Dữ liệu đơn hàng không được để trống");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidOrderException("Số lượng (quantity) phải lớn hơn 0");
        }
        if (request.getCustomerId() == null || request.getProductId() == null) {
            throw new InvalidOrderException("customerId và productId không được để trống");
        }

        BigDecimal unitPrice = productServiceClient.getProductPrice(request.getProductId());

        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .build();

        try {
            return orderRepository.save(order);
        } catch (DataAccessException ex) {
            log.error("Lỗi khi lưu đơn hàng vào DB: {}", ex.getMessage(), ex);
            throw new DatabaseSaveException("Lưu đơn hàng vào cơ sở dữ liệu thất bại: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Lỗi không xác định khi lưu đơn hàng vào DB: {}", ex.getMessage(), ex);
            throw new DatabaseSaveException("Lưu đơn hàng vào cơ sở dữ liệu thất bại: " + ex.getMessage(), ex);
        }
    }
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        if (id == null) {
            throw new InvalidOrderException("id đơn hàng không được để trống");
        }
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Không tìm thấy đơn hàng với id: " + id));
    }
}
