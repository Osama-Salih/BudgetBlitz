package com.budget_blitz.overview;

import com.budget_blitz.overview.response.CategorySummaryResponse;
import com.budget_blitz.overview.response.OverviewResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OverviewMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OverviewResponse toOverviewResponse(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal savings, List<CategorySummaryResponse> categoryBreakdown);
}
