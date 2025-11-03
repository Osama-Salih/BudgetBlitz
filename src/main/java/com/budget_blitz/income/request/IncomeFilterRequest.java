package com.budget_blitz.income.request;

import com.budget_blitz.common.FilterRequest;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for filtering income records")
public class IncomeFilterRequest extends FilterRequest {}
