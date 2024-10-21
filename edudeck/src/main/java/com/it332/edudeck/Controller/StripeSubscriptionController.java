package com.it332.edudeck.Controller;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.param.CustomerListParams;
import com.stripe.param.SubscriptionListParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StripeSubscriptionController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/subscription")
    public SubscriptionResponse getSubscription(@RequestBody SubscriptionRequest request) {
        Stripe.apiKey = stripeApiKey; // Your Stripe secret key

        try {
            // Fetch customer by email
            CustomerListParams customerParams = CustomerListParams.builder()
                    .setEmail(request.getEmail())
                    .setLimit(1L)
                    .build();

            List<Customer> customers = Customer.list(customerParams).getData();

            if (customers.isEmpty()) {
                return new SubscriptionResponse(false, null, null); // No customer found with this email
            }

            // Fetch the first customer (assume one email per customer)
            Customer customer = customers.get(0);

            // Now fetch subscriptions associated with the customer's ID
            SubscriptionListParams subscriptionParams = SubscriptionListParams.builder()
                    .setCustomer(customer.getId()) // Filter by customer ID
                    .setLimit(1L) // Limit to 1 subscription
                    .build();

            List<com.stripe.model.Subscription> subscriptions = com.stripe.model.Subscription.list(subscriptionParams).getData();

            if (subscriptions.isEmpty()) {
                return new SubscriptionResponse(false, null, null); // No active subscriptions
            }

            // Get the first subscription (if present)
            com.stripe.model.Subscription subscription = subscriptions.get(0);

            // Get the next billing date as Unix timestamp
            Long nextBillingDate = subscription.getCurrentPeriodEnd();

            // Convert Unix timestamp to human-readable format
            String nextBillingDateFormatted = convertUnixToReadableDate(nextBillingDate);

            // Return active subscription status, next billing date, and formatted date
            return new SubscriptionResponse(true, nextBillingDate, nextBillingDateFormatted);
        } catch (Exception e) {
            e.printStackTrace();
            return new SubscriptionResponse(false, null, null); // In case of error
        }
    }

    // Convert Unix timestamp to human-readable format
    private String convertUnixToReadableDate(Long unixTimestamp) {
        if (unixTimestamp == null) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Define the request and response DTOs
    public static class SubscriptionRequest {
        private String email;

        // Getter for email
        public String getEmail() {
            return email;
        }

        // Setter for email
        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class SubscriptionResponse {
        private boolean active;
        private Long nextBillingDate;
        private String nextBillingDateFormatted;

        // Constructor
        public SubscriptionResponse(boolean active, Long nextBillingDate, String nextBillingDateFormatted) {
            this.active = active;
            this.nextBillingDate = nextBillingDate;
            this.nextBillingDateFormatted = nextBillingDateFormatted;
        }

        // Getter for active
        public boolean isActive() {
            return active;
        }

        // Getter for nextBillingDate
        public Long getNextBillingDate() {
            return nextBillingDate;
        }

        // Getter for nextBillingDateFormatted
        public String getNextBillingDateFormatted() {
            return nextBillingDateFormatted;
        }
    }
}
