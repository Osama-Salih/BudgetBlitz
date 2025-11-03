package com.budget_blitz.expense;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {

    public static Specification<Expense> belongsToUser(Integer userId) {
        return ((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Expense> dateBetween(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            if (fromDate == null || toDate == null) {
                return cb.conjunction();
            }
            return cb.between(root.get("date"), fromDate, toDate);
        };
    }

    public static Specification<Expense> amountGreaterThen(BigDecimal amount) {
        return (root, query, cb) -> {
            if (amount == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("amount"), amount);
        };
    }

    public static Specification<Expense> amountLessThen(BigDecimal amount) {
        return (root, query, cb) -> {
            if (amount == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("amount"), amount);
        };
    }

    public static Specification<Expense> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null) {
                return cb.conjunction();
            }
            return cb.like(root.get("description"), "%" + keyword + "%");
        };
    }

    public static Specification<Expense> belongToCategory(String category) {
        return ((root, query, cb) -> {
            if (category == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("category").get("name"), category);
        });
    }
}