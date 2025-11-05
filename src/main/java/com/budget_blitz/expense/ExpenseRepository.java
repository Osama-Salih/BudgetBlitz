package com.budget_blitz.expense;

import com.budget_blitz.overview.response.CategorySummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @Query("""
            SELECT SUM(e.amount)
            FROM Expense e
            WHERE e.user.id =:userId
            AND (:month IS NULL OR MONTH(e.date) = :month)
            AND (:year IS NULL OR YEAR(e.date) = :year)
            """)
    BigDecimal getTotalExpenses(@Param("userId") Integer userId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("""
            SELECT new com.budget_blitz.overview.response.CategorySummaryResponse(c.name AS categoryName, SUM(e.amount) AS total)
            FROM Expense e
            JOIN FETCH Category c
            ON e.category.id = c.id
            WHERE e.user.id =:userId
            AND (:month IS NULL OR MONTH(e.date) = :month)
            AND (:year IS NULL OR YEAR(e.date) = :year)
            GROUP BY c.name
            """)
    List<CategorySummaryResponse> getTotalExpensesByCategory(@Param("userId") Integer userId, @Param("month") Integer month, @Param("year") Integer year);
}