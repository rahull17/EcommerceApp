package com.example.server.service;

import com.example.server.dto.req.OrderRequestDTO;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalIntegrationService {

    private final APIContext apiContext;

    public Payment createPayment(double total, List<OrderRequestDTO.CartItem> items,
                                 String cancelUrl, String successUrl) throws PayPalRESTException {

        // 1. Amount
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", total));

        // 2. Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Purchase from Shop");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // 3. Payer
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // 4. Payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // 5. Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // 6. Create payment
        Payment created = payment.create(apiContext);

        for (Links link : created.getLinks()) {
            if ("approval_url".equals(link.getRel())) {
                if (link.getHref().contains("cgi-bin/webscr")) {
                    String fixedUrl = link.getHref()
                            .replace("cgi-bin/webscr?cmd=_express-checkout&token=", "checkoutnow?token=");
                    link.setHref(fixedUrl);
                }
            }
        }


        return created;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}
