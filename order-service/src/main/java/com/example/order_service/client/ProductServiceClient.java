package com.example.order_service.client;

import com.example.order_service.dto.ProductDTO;
import com.example.order_service.exception.InvalidOrderException;
import com.example.order_service.exception.ProductServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://localhost:8082/api/v1/products}")
    private String productServiceUrl;

    /**
     * Gọi API sang Product Service để lấy thông tin sản phẩm bằng RestTemplate.
     * Áp dụng Exception Handling để bắt các trường hợp sự cố:
     * - Service bị tắt / mất mạng / timeout (ResourceAccessException)
     * - Sản phẩm không tồn tại (404 NotFound)
     * - Lỗi server bên Product Service (500 Internal Server Error)
     */
    public ProductDTO getProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId không được để trống");
        }

        String url = productServiceUrl + "/" + productId;
        log.info("Order Service đang gọi API sang Product Service: {}", url);

        try {
            ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);
            if (product == null) {
                log.warn("Product Service trả về dữ liệu rỗng cho productId: {}", productId);
                throw new ProductServiceUnavailableException("Dịch vụ sản phẩm không trả về dữ liệu hợp lệ");
            }
            log.info("Lấy thông tin sản phẩm thành công: id={}, name={}, price={}, stock={}",
                    product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
            return product;
        } catch (ResourceAccessException ex) {
            // Khi Product Service bị STOP / tắt kết nối hoặc quá thời gian chờ (Timeout)
            log.error("Kết nối sang Product Service thất bại (Service có thể đã bị dừng hoặc mạng chậm) cho productId {}: {}", productId, ex.getMessage());
            throw new ProductServiceUnavailableException("Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau", ex);
        } catch (HttpClientErrorException.NotFound ex) {
            // Khi Product Service trả về HTTP 404 (Không tìm thấy sản phẩm)
            log.warn("Không tìm thấy sản phẩm với id {} trong Product Service: {}", productId, ex.getMessage());
            throw new InvalidOrderException("Không tìm thấy sản phẩm với id: " + productId);
        } catch (HttpStatusCodeException ex) {
            // Khi Product Service trả về HTTP 5xx hoặc các mã lỗi khác
            log.error("Product Service phản hồi lỗi HTTP {} cho productId {}: {}", ex.getStatusCode(), productId, ex.getResponseBodyAsString());
            throw new ProductServiceUnavailableException("Dịch vụ sản phẩm gặp sự cố nội bộ (" + ex.getStatusCode().value() + "), vui lòng thử lại sau", ex);
        } catch (Exception ex) {
            // Lỗi không xác định khác
            log.error("Lỗi ngoại lệ khi gọi sang Product Service cho productId {}: {}", productId, ex.getMessage(), ex);
            throw new ProductServiceUnavailableException("Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau", ex);
        }
    }

    /**
     * Lấy giá sản phẩm từ Product Service
     */
    public BigDecimal getProductPrice(Long productId) {
        ProductDTO product = getProduct(productId);
        return BigDecimal.valueOf(product.getPrice());
    }

    /**
     * Phương án Nâng cao: Fallback Data
     * Trả về thông tin sản phẩm mặc định khi Product Service gặp lỗi để luồng nghiệp vụ không bị ngắt hoàn toàn.
     */
    public ProductDTO getProductWithFallback(Long productId) {
        try {
            return getProduct(productId);
        } catch (ProductServiceUnavailableException ex) {
            log.warn("[FALLBACK] Kích hoạt dữ liệu mặc định cho productId {} do Product Service không khả dụng", productId);
            return ProductDTO.builder()
                    .id(productId)
                    .name("Sản phẩm mặc định (Fallback)")
                    .price(100.0)
                    .stockQuantity(999)
                    .build();
        }
    }
}
