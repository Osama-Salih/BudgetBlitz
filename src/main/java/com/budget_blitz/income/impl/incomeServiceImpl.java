package com.budget_blitz.income.impl;

import com.budget_blitz.common.PageResponse;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.income.*;
import com.budget_blitz.income.request.AddIncomeRequest;
import com.budget_blitz.income.request.IncomeFilterRequest;
import com.budget_blitz.income.request.UpdateIncomeRequest;
import com.budget_blitz.income.response.IncomeResponse;
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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class incomeServiceImpl implements IncomeService {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;

    @Override
    public PageResponse<IncomeResponse> findAll(final IncomeFilterRequest filter, final Integer userId) {

        final Specification<Income> spec = IncomeSpecification.belongsToUser(userId)
                .and(IncomeSpecification.dateBetween(filter.getFromDate(), filter.getToDate()))
                .and(IncomeSpecification.amountGreaterThen(filter.getMinAmount()))
                .and(IncomeSpecification.amountLessThen(filter.getMaxAmount()))
                .and(IncomeSpecification.containKeyword(filter.getKeyword()));


        final String[] sortParam = filter.getSort().split(",");
        final Sort sort = Sort.by(Sort.Direction.fromString(sortParam[1]), sortParam[0]);

        final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        final Page<Income> incomes = this.incomeRepository.findAll(spec, pageable);
        final List<IncomeResponse> responses = incomes.stream().map(incomeMapper::toIncomeResponse).toList();

        return new PageResponse<>(
                responses,
                incomes.getNumber(),
                incomes.getSize(),
                incomes.getTotalElements(),
                incomes.getTotalPages(),
                incomes.isFirst(),
                incomes.isLast()
        );
    }

    @Override
    public IncomeResponse addIncome(final AddIncomeRequest request, final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        user.getRoles()
                .stream()
                .filter(r -> r.getName().equals("ROLE_USER"))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCESS_DENIED));

        final Income createdIncome = this.incomeMapper.toIncome(request, user);
        this.incomeRepository.save(createdIncome);
        log.info("Saving income: {}", createdIncome);
        return this.incomeMapper.toIncomeResponse(createdIncome);
    }

    @Override
    public IncomeResponse getIncomeById(final Integer incomeId, final Integer userId) {
        final Income income = this.incomeRepository.findById(incomeId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found"));

        if(!Objects.equals(income.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return this.incomeMapper.toIncomeResponse(income);
    }

    @Override
    public IncomeResponse updateIncomeById(final UpdateIncomeRequest request, final Integer incomeId, final Integer userId) {
        final Income income = this.incomeRepository.findById(incomeId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found"));

        if(!Objects.equals(income.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        this.incomeMapper.updateIncomeRequestToIncome(request, income);
        log.debug("Updated income: {}", income);
        this.incomeRepository.save(income);
        return this.incomeMapper.toIncomeResponse(income);
    }

    @Override
    public void deleteIncomeById(final Integer incomeId, final Integer userId) {
        final Income income = this.incomeRepository.findById(incomeId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found"));

        if(!Objects.equals(income.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        log.info("Deleting income with id={} for user={}", incomeId, userId);
        this.incomeRepository.deleteById(incomeId);
    }
}