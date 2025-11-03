package com.budget_blitz.expense.impl;

import com.budget_blitz.category.Category;
import com.budget_blitz.category.CategoryRepository;
import com.budget_blitz.common.PageResponse;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.expense.*;
import com.budget_blitz.expense.request.AddExpenseRequest;
import com.budget_blitz.expense.request.ExpenseFilterRequest;
import com.budget_blitz.expense.request.UpdateExpenseRequest;
import com.budget_blitz.expense.response.ExpenseResponse;
import com.budget_blitz.users.User;
import com.budget_blitz.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public PageResponse<ExpenseResponse> findAll(final ExpenseFilterRequest filter, final Integer userId) {

        final Specification<Expense> spec = ExpenseSpecification.belongsToUser(userId)
                .and(ExpenseSpecification.dateBetween(filter.getFromDate(), filter.getToDate()))
                .and(ExpenseSpecification.amountGreaterThen(filter.getMinAmount()))
                .and(ExpenseSpecification.amountLessThen(filter.getMaxAmount()))
                .and(ExpenseSpecification.belongToCategory(filter.getCategoryName()))
                .and(ExpenseSpecification.containKeyword(filter.getKeyword()));


        final String[] sortParam = filter.getSort().split(",");
        final Sort sort = Sort.by(Sort.Direction.fromString(sortParam[1]), sortParam[0]);

        final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        final Page<Expense> expenses = this.expenseRepository.findAll(spec, pageable);
        final List<ExpenseResponse> responses = expenses.stream().map(expenseMapper::toExpenseResponse).toList();

        return new PageResponse<>(
                responses,
                expenses.getNumber(),
                expenses.getSize(),
                expenses.getTotalElements(),
                expenses.getTotalPages(),
                expenses.isFirst(),
                expenses.isLast()
        );
    }

    @Override
    @Transactional
    public ExpenseResponse addExpense(final AddExpenseRequest request, final Integer userId) {

        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        final Category category = this.categoryRepository.findByCategoryIdAndUserId(request.getCategoryId(), user.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        boolean isUserRole = user.getRoles()
                                 .stream()
                                 .anyMatch(role -> role.getName().equals("ROLE_USER"));
        if (!isUserRole) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        boolean exists = expenseRepository.existsByCategoryIdAndUserIdAndAmountAndDate(
                category.getId(),
                user.getId(),
                request.getAmount(),
                request.getDate()
        );

        if (exists) {
            throw new BusinessException(ErrorCode.EXPENSE_ALREADY_EXISTS);
        }

        final Expense expense  = this.expenseMapper.toExpense(request, user, category);
        this.expenseRepository.save(expense);
        log.info("Saving expense: {}", expense);

        return this.expenseMapper.toExpenseResponse(expense);
    }

    @Override
    public ExpenseResponse getExpenseById(final Integer expenseId, final Integer userId) {
        final Expense expense = this.expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        if(!Objects.equals(expense.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return this.expenseMapper.toExpenseResponse(expense);
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpenseById(final UpdateExpenseRequest request, final Integer expenseId, final Integer userId) {
        final Expense expense = this.expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        if(!Objects.equals(expense.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if (request.getCategoryId() != null) {
            this.categoryRepository.findByCategoryIdAndUserId(request.getCategoryId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        this.expenseMapper.updateExpenseRequestToExpense(request, expense);
        log.debug("Updated expense: {}", expense);
        this.expenseRepository.save(expense);

        return this.expenseMapper.toExpenseResponse(expense);
    }

    @Override
    public void deleteExpenseById(final Integer expenseId, final Integer userId) {
        final Expense expense = this.expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));

        if(!Objects.equals(expense.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        log.info("Deleting expense with id={} for user={}", expenseId, userId);
        this.expenseRepository.deleteById(expenseId);
    }
}