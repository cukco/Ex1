package com.example.order_service;

import com.example.order_service.client.ProductServiceClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.exception.DatabaseSaveException;
import com.example.order_service.exception.InvalidOrderException;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRequest = OrderRequest.builder()
                .customerId(10L)
                .productId(1L)
                .quantity(3)
                .build();
    }

    @Test
    @DisplayName("createOrder - Tính toán đúng totalAmount và lưu Order vào DB")
    void testCreateOrder_Success() {
        // Product 1L giá 100.00
        when(productServiceClient.getProductPrice(1L)).thenReturn(new BigDecimal("100.00"));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });

        Order created = orderService.createOrder(sampleRequest);

        assertNotNull(created);
        assertEquals(100L, created.getId());
        assertEquals(10L, created.getCustomerId());
        assertEquals(1L, created.getProductId());
        // 100.00 * 3 = 300.00
        assertEquals(new BigDecimal("300.00"), created.getTotalAmount());
        assertNotNull(created.getOrderDate());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("createOrder - Ném InvalidOrderException khi quantity <= 0")
    void testCreateOrder_InvalidQuantity_ThrowsException() {
        sampleRequest.setQuantity(0);

        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
                () -> orderService.createOrder(sampleRequest));

        assertTrue(ex.getMessage().contains("phải lớn hơn 0"));
        verifyNoInteractions(productServiceClient);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("createOrder - Ném DatabaseSaveException khi DB lưu thất bại")
    void testCreateOrder_DatabaseError_ThrowsDatabaseSaveException() {
        when(productServiceClient.getProductPrice(1L)).thenReturn(new BigDecimal("100.00"));
        when(orderRepository.save(any(Order.class)))
                .thenThrow(new DataIntegrityViolationException("Database constraint error"));

        DatabaseSaveException ex = assertThrows(DatabaseSaveException.class,
                () -> orderService.createOrder(sampleRequest));

        assertTrue(ex.getMessage().contains("Lưu đơn hàng vào cơ sở dữ liệu thất bại"));
    }

    @Test
    @DisplayName("getOrderById - Thành công khi ID tồn tại")
    void testGetOrderById_Success() {
        Order mockOrder = Order.builder()
                .id(1L)
                .customerId(10L)
                .productId(1L)
                .totalAmount(new BigDecimal("300.00"))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getCustomerId());
    }

    @Test
    @DisplayName("getOrderById - Ném OrderNotFoundException khi ID không tồn tại")
    void testGetOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(999L));
    }
}
