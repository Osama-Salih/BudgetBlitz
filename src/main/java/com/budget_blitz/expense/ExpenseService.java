package com.budget_blitz.expense;

import com.budget_blitz.common.PageResponse;
import com.budget_blitz.expense.request.AddExpenseRequest;
import com.budget_blitz.expense.request.ExpenseFilterRequest;
import com.budget_blitz.expense.request.UpdateExpenseRequest;
import com.budget_blitz.expense.response.ExpenseResponse;

public interface ExpenseService {

    ExpenseResponse addExpense(final AddExpenseRequest request, final Integer userId);
    ExpenseResponse getExpenseById(final Integer expenseId,final Integer userId);
    ExpenseResponse updateExpenseById(final UpdateExpenseRequest request, final Integer expenseId, final Integer userId);
    void deleteExpenseById(final Integer expenseId, final Integer userId);
    PageResponse<ExpenseResponse> findAll(final ExpenseFilterRequest filterRequest, final Integer userId);
}