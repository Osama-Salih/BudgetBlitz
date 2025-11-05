package com.budget_blitz.overview.impl;

import com.budget_blitz.expense.ExpenseRepository;
import com.budget_blitz.income.IncomeRepository;
import com.budget_blitz.overview.OverviewMapper;
import com.budget_blitz.overview.OverviewService;
import com.budget_blitz.overview.request.OverviewFilterRequest;
import com.budget_blitz.overview.response.CategorySummaryResponse;
import com.budget_blitz.overview.response.OverviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverviewServiceImpl implements OverviewService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final OverviewMapper overviewMapper;
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Override
    public OverviewResponse getOverview(final OverviewFilterRequest filterRequest, final Integer userId) {

        final BigDecimal totalIncome = this.incomeRepository.getTotalIncome(userId, filterRequest.getMonth(), filterRequest.getYear());
        final BigDecimal totalExpenses = this.expenseRepository.getTotalExpenses(userId, filterRequest.getMonth(), filterRequest.getYear());

        final BigDecimal income = totalIncome != null ? totalIncome : ZERO;
        final BigDecimal expense = totalExpenses != null ? totalExpenses : ZERO;
        final BigDecimal savings = income.subtract(expense);

        final List<CategorySummaryResponse> categoryBreakdown = Optional.ofNullable(this.expenseRepository
                        .getTotalExpensesByCategory(userId, filterRequest.getMonth(), filterRequest.getYear()))
                        .orElse(Collections.emptyList());

        log.debug("Generating overview for user {}, month {}, year {}", userId,filterRequest.getMonth(), filterRequest.getYear());
        return this.overviewMapper.toOverviewResponse(totalIncome, totalExpenses, savings, categoryBreakdown);
    }
}