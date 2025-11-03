package com.budget_blitz.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {

    @Query("""
            SELECT COUNT(e) > 0
            FROM Expense e
            WHERE e.user.id =:userId
            AND e.category.id =:categoryId
            AND e.amount =:amount
            AND e.date =:date
            """)
    boolean existsByCategoryIdAndUserIdAndAmountAndDate(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId,
                                                        @Param("amount") BigDecimal amount, @Param("date") LocalDate date);
}