package com.budget_blitz.expense;

import com.budget_blitz.category.Category;
import com.budget_blitz.common.BaseEntity;
import com.budget_blitz.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "expenses",
        indexes = {
            @Index(name = "idx_expense_user", columnList = "user_id"),
            @Index(name = "idx_expense_category", columnList = "category_id"),
            @Index(name = "idx_expense_date", columnList = "date")
        }
)
public class Expense extends BaseEntity {

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "description", nullable = true, length = 250)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}