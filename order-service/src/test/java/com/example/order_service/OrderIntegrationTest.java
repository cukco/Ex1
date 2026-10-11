package com.example.order_service;

import com.example.order_service.client.ProductServiceClient;
import com.example.order_service.exception.ProductServiceUnavailableException;
import com.example.order_service.repository.OrderRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private ProductServiceClient productServiceClient;

    @Test
    @DisplayName("Integration: POST /api/v1/orders tạo order thực sự vào PostgreSQL và GET /api/v1/orders/{id}")
    void testEndToEndOrderFlow() throws Exception {
        when(productServiceClient.getProductPrice(2L)).thenReturn(new BigDecimal("250.00"));

        String jsonBody = """
                {
                    "customerId": 100,
                    "productId": 2,
                    "quantity": 3
                }
                """;

        // 1. POST tạo order
        MvcResult postResult = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value(100))
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.totalAmount").value(750.00)) // 250.00 * 3
                .andReturn();

        Number orderIdNumber = JsonPath.read(postResult.getResponse().getContentAsString(), "$.id");
        Long orderId = orderIdNumber.longValue();
        assertNotNull(orderId);

        // 2. Kiểm tra tồn tại trong DB PostgreSQL
        assertTrue(orderRepository.findById(orderId).isPresent());

        // 3. GET lấy order
        mockMvc.perform(get("/api/v1/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.customerId").value(100))
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.totalAmount").value(750.00));
    }

    @Test
    @DisplayName("Integration: POST /api/v1/orders khi Product Service bị sập/tắt trả về 503 và ApiResponseError")
    void testEndToEndProductServiceUnavailable() throws Exception {
        when(productServiceClient.getProductPrice(2L))
                .thenThrow(new ProductServiceUnavailableException("Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau"));

        String jsonBody = """
                {
                    "customerId": 100,
                    "productId": 2,
                    "quantity": 3
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/orders"));
    }

    @Test
    @DisplayName("Integration: POST /api/v1/orders với quantity <= 0 trả về 400 Bad Request và ApiResponseError")
    void testEndToEndInvalidQuantity() throws Exception {
        String jsonBody = """
                {
                    "customerId": 100,
                    "productId": 2,
                    "quantity": -1
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
}
