package com.budget_blitz.income;

import com.budget_blitz.common.PageResponse;
import com.budget_blitz.income.request.AddIncomeRequest;
import com.budget_blitz.income.request.IncomeFilterRequest;
import com.budget_blitz.income.request.UpdateIncomeRequest;
import com.budget_blitz.income.response.IncomeResponse;

public interface IncomeService {

   IncomeResponse addIncome(final AddIncomeRequest request, final Integer userId);
   IncomeResponse getIncomeById(final Integer incomeId,final Integer userId);
   IncomeResponse updateIncomeById(final UpdateIncomeRequest request, final Integer incomeId, final Integer userId);
   void deleteIncomeById(final Integer incomeId, final Integer userId);
   PageResponse<IncomeResponse> findAll(final IncomeFilterRequest filterRequest, final Integer userId);
}
