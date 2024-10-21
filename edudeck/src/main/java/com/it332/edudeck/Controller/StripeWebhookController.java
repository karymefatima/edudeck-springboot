//package com.it332.edudeck.Controller;
//
//import com.stripe.Stripe;
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.model.Event;
//import com.stripe.model.Subscription;
//import com.stripe.net.Webhook;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//public class StripeWebhookController {
//
//    @Value("${stripe.api.key}")
//    private String stripeApiKey;
//
//    @Value("${stripe.webhook.secret}")
//    private String stripeWebhookSecret;
//
//    private boolean isSubscribed; // Stores the subscription status of the user
//
//    // Getter for isSubscribed
//    public boolean isSubscribed() {
//        return isSubscribed;
//    }
//
//    // Setter for isSubscribed
//    public void setSubscribed(boolean isSubscribed) {
//        this.isSubscribed = isSubscribed;
//    }
//
//    @PostMapping("/webhook")
//    public String handleStripeWebhook(@RequestBody String payload,
//                                      @RequestHeader("Stripe-Signature") String sigHeader) {
//        Stripe.apiKey = stripeApiKey; // Your Stripe secret key
//
//        // Verify the webhook signature to ensure it comes from Stripe
//        Event event;
//        try {
//            event = Webhook.constructEvent(
//                    payload, sigHeader, stripeWebhookSecret
//            );
//        } catch (SignatureVerificationException e) {
//            // Invalid signature
//            return "Invalid signature";
//        }
//
//        // Handle the event
//        switch (event.getType()) {
//            case "customer.subscription.created":
//                handleSubscriptionCreated(event);
//                break;
//            case "customer.subscription.updated":
//                handleSubscriptionUpdated(event);
//                break;
//            case "customer.subscription.deleted":
//                handleSubscriptionDeleted(event);
//                break;
//            default:
//                // Unexpected event type
//                return "Unhandled event type";
//        }
//
//        return "Success";
//    }
//
//    private void handleSubscriptionCreated(Event event) {
//        // Retrieve the subscription object from the event
//        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
//                .getObject().orElse(null);
//
//        if (subscription != null) {
//            String customerId = subscription.getCustomer();
//            Long nextBillingDate = subscription.getCurrentPeriodEnd();
//
//            // Update user's subscription status
//            setSubscribed(true);
//
//            // Process the subscription creation event
//            System.out.println("Subscription created for customer: " + customerId);
//            System.out.println("Next billing date: " + nextBillingDate);
//        }
//    }
//
//    private void handleSubscriptionUpdated(Event event) {
//        // Retrieve the subscription object from the event
//        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
//                .getObject().orElse(null);
//
//        if (subscription != null) {
//            String customerId = subscription.getCustomer();
//            Long nextBillingDate = subscription.getCurrentPeriodEnd();
//
//            // Based on the subscription status, update the subscription flag
//            boolean isActive = subscription.getStatus().equals("active");
//            setSubscribed(isActive);
//
//            // Process the subscription update event
//            System.out.println("Subscription updated for customer: " + customerId);
//            System.out.println("Next billing date: " + nextBillingDate);
//            System.out.println("Is subscribed: " + isSubscribed());
//        }
//    }
//
//    private void handleSubscriptionDeleted(Event event) {
//        // Retrieve the subscription object from the event
//        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
//                .getObject().orElse(null);
//
//        if (subscription != null) {
//            String customerId = subscription.getCustomer();
//
//            // Mark the user as unsubscribed when the subscription is deleted
//            setSubscribed(false);
//
//            // Process the subscription deletion event
//            System.out.println("Subscription deleted for customer: " + customerId);
//            System.out.println("Is subscribed: " + isSubscribed());
//        }
//    }
//}
