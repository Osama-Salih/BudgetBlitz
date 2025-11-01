package com.budget_blitz.income;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class IncomeSpecification {

    public static Specification<Income> belongsToUser(Integer userId) {
        return ((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Income> dateBetween(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            if (fromDate == null || toDate == null) {
                return cb.conjunction();
            }
           return cb.between(root.get("date"), fromDate, toDate);
        };
    }

    public static Specification<Income> amountGreaterThen(Double amount) {
        return (root, query, cb) -> {
            if (amount == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("amount"), amount);
        };
    }

    public static Specification<Income> amountLessThen(Double amount) {
        return (root, query, cb) -> {
            if (amount == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("amount"), amount);
        };
    }

    public static Specification<Income> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null) {
                return cb.conjunction();
            }
            return cb.like(root.get("description"), "%" + keyword + "%");
        };
    }
}
