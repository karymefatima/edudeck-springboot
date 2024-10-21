package com.it332.edudeck.Controller;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StripeCustomerPortalController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @GetMapping("/create-customer-portal")
    public String createCustomerPortal(@RequestParam String email) throws Exception {
        Stripe.apiKey = stripeApiKey;

        // Retrieve the customer by email
        Customer customer = getCustomerByEmail(email);
        if (customer == null) {
            throw new Exception("Customer not found");
        }

        // Create a session for Stripe's Customer Portal
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customer.getId())
                .setReturnUrl("https://edudeck.vercel.app/pricing")  // URL to redirect after managing subscription
                .build();

        Session session = Session.create(params);
        return session.getUrl(); // This is the URL to redirect the user to
    }

    // Helper function to get a customer by email
    private Customer getCustomerByEmail(String email) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        List<Customer> customers = Customer.list(params).getData();
        return ((List<?>) customers).isEmpty() ? null : customers.get(0);
    }
}

