package com.ecinema.app.domain.enums;

public enum CouponType {
    TICKET_COUPON {
        @Override
        public String toString() {
            return "Ticket Coupon";
        }
    },
    FOOD_DRINK_COUPON {
        @Override
        public String toString() {
            return "Food-Drink Coupon";
        }
    }
}
