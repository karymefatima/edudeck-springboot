package com.it332.edudeck.Controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeCheckoutSessionController {

    // Inject the Stripe API key from the application properties
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/checkoutsession")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, String> requestData) {
        Stripe.apiKey = stripeApiKey;

        // Extract email from the request
        String email = requestData.get("email");

        // Set the productId to the specific price value
        String productId = "price_1PNBLnEWlqrSRLbyYaDHGaHG";

        // Create session for subscription mode with customer email and product ID
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl("https://edudeck.vercel.app/pricing?success=true")
                    .setCancelUrl("https://edudeck.vercel.app/pricing?success=false")// SET THE DEFAULT URL WHEN DEPLOYED
                    .setCustomerEmail(email) // The email of the customer
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(productId) // Use hardcoded Price ID
                                    .setQuantity(1L)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            // Prepare response data containing the session URL
            Map<String, String> responseData = new HashMap<>();
            responseData.put("checkoutUrl", session.getUrl());

            return responseData;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Stripe session creation failed.");
        }
    }
}
