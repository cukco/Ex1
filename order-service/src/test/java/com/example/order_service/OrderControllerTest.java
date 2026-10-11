package com.example.order_service;

import com.example.order_service.controller.OrderController;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.exception.DatabaseSaveException;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("POST /api/v1/orders - Thành công khi dữ liệu hợp lệ")
    void testCreateOrder_Success() throws Exception {
        Order mockOrder = Order.builder()
                .id(1L)
                .customerId(10L)
                .productId(20L)
                .orderDate(LocalDateTime.now())
                .totalAmount(new BigDecimal("200.00"))
                .build();

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(mockOrder);

        String jsonBody = """
                {
                    "customerId": 10,
                    "productId": 20,
                    "quantity": 2
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(10))
                .andExpect(jsonPath("$.productId").value(20))
                .andExpect(jsonPath("$.totalAmount").value(200.00))
                .andExpect(jsonPath("$.orderDate").exists());
    }

    @Test
    @DisplayName("POST /api/v1/orders - Trả về 400 và ApiResponseError khi quantity <= 0")
    void testCreateOrder_InvalidQuantity_Returns400() throws Exception {
        String jsonBody = """
                {
                    "customerId": 10,
                    "productId": 20,
                    "quantity": 0
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/orders"));
    }

    @Test
    @DisplayName("POST /api/v1/orders - Trả về 400 khi quantity là số âm")
    void testCreateOrder_NegativeQuantity_Returns400() throws Exception {
        String jsonBody = """
                {
                    "customerId": 10,
                    "productId": 20,
                    "quantity": -5
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/v1/orders - Trả về 500 và ApiResponseError khi lưu DB thất bại")
    void testCreateOrder_DatabaseFailure_Returns500() throws Exception {
        when(orderService.createOrder(any(OrderRequest.class)))
                .thenThrow(new DatabaseSaveException("Lưu đơn hàng vào cơ sở dữ liệu thất bại"));

        String jsonBody = """
                {
                    "customerId": 10,
                    "productId": 20,
                    "quantity": 2
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Lưu vào DB thất bại: Lưu đơn hàng vào cơ sở dữ liệu thất bại"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/orders"));
    }

    @Test
    @DisplayName("GET /api/v1/orders/{id} - Thành công trả về thông tin đơn hàng")
    void testGetOrderById_Success() throws Exception {
        Order mockOrder = Order.builder()
                .id(1L)
                .customerId(10L)
                .productId(20L)
                .orderDate(LocalDateTime.now())
                .totalAmount(new BigDecimal("200.00"))
                .build();

        when(orderService.getOrderById(1L)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(10))
                .andExpect(jsonPath("$.productId").value(20))
                .andExpect(jsonPath("$.totalAmount").value(200.00));
    }

    @Test
    @DisplayName("GET /api/v1/orders/{id} - Trả về 404 khi không tìm thấy đơn hàng")
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(999L))
                .thenThrow(new OrderNotFoundException("Không tìm thấy đơn hàng với id: 999"));

        mockMvc.perform(get("/api/v1/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Không tìm thấy đơn hàng với id: 999"));
    }
}
