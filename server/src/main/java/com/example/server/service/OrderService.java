package com.example.server.service;

import com.example.server.dto.req.OrderRequestDTO;
import com.example.server.dto.res.ApiResponse;
import com.example.server.dto.res.PaymentCreationData;
import com.example.server.entity.Order;
import com.example.server.repository.CartRepository;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.ProductRepository;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PayPalIntegrationService payPalService;


    public ApiResponse<PaymentCreationData> createPayment(OrderRequestDTO request) throws PayPalRESTException {
        // Check stock availability
        for (var item : request.getCartItems()) {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getTotalStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getTitle());
            }
        }

        Payment payment = payPalService.createPayment(
                request.getTotalAmount(),
                request.getCartItems(),
                "http://localhost:5173/shop/paypal-cancel",
                "http://localhost:5173/shop/paypal-return"
        );

        // Map DTO cart items to entity
        List<Order.CartItem> entityCartItems = request.getCartItems().stream()
                .map(dtoItem -> Order.CartItem.builder()
                        .productId(dtoItem.getProductId())
                        .title(dtoItem.getTitle())
                        .image(dtoItem.getImage())
                        .price(dtoItem.getPrice())
                        .quantity(dtoItem.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // Map address info
        Order.AddressInfo address = Order.AddressInfo.builder()
                .addressId(request.getAddressInfo().getAddressId())
                .address(request.getAddressInfo().getAddress())
                .city(request.getAddressInfo().getCity())
                .pincode(request.getAddressInfo().getPincode())
                .phone(request.getAddressInfo().getPhone())
                .notes(request.getAddressInfo().getNotes())
                .build();

        // Save pending order
        Order order = Order.builder()
                .userId(request.getUserId())
                .cartId(request.getCartId())
                .cartItems(entityCartItems)
                .addressInfo(address)
                .orderStatus("PENDING")
                .paymentMethod("PAYPAL")
                .paymentStatus("PENDING")
                .totalAmount(request.getTotalAmount())
                .paymentId(payment.getId())
                .build();

        orderRepository.save(order);

        // Extract approval URL from PayPal response
        String approvalUrl = payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No approval URL returned by PayPal"))
                .getHref();

        PaymentCreationData responseData = PaymentCreationData.builder()
                .approvalURL(approvalUrl)
                .orderId(order.getId())
                .build();

        return new ApiResponse<>(true, "Payment created successfully", responseData);
    }


    @Transactional
    public void capturePayment(String paymentId, String payerId) throws Exception {
        // Execute PayPal payment
        payPalService.executePayment(paymentId, payerId);

        // Find order
        Order order = orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Order not found for paymentId " + paymentId));

        // Deduct stock
        for (var item : order.getCartItems()) {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setTotalStock(product.getTotalStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setOrderStatus("confirmed");
        order.setPaymentStatus("paid");
        order.setPayerId(payerId);
        orderRepository.save(order);

        cartRepository.deleteById(order.getCartId());
    }

    @Transactional
    public Order markOrderAsPaid(Long orderId, String paymentId, String payerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setOrderStatus("COMPLETED");
        order.setPaymentStatus("PAID");
        order.setPaymentId(paymentId);
        order.setPayerId(payerId);

        Order saved = orderRepository.save(order);


        if (order.getCartId() != null) {
            cartRepository.deleteById(order.getCartId());
        }

        return saved;
    }

    public List<Order> getAllOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }


    public Order getOrderDetails(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
}
