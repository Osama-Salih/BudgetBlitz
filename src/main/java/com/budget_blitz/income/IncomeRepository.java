package com.budget_blitz.income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IncomeRepository extends JpaRepository<Income, Integer>, JpaSpecificationExecutor<Income> {

    @Query("""
            SELECT COUNT(i) > 0
            FROM Income i
            WHERE i.user.id =:userId
            AND i.amount =:amount
            AND i.date =:date
           
            """)
    boolean existsByUserIdAndAmountAndDate(@Param("userId") Integer userId, @Param("amount") BigDecimal amount, @Param("date") LocalDate date);
}
