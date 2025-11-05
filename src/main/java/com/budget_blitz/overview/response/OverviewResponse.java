package com.budget_blitz.overview.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Overall financial summary including totals and breakdown")
public class OverviewResponse {

    @Schema(description = "Total income for the selected period", example = "80000.00")
    private BigDecimal totalIncome;

    @Schema(description = "Total expenses for the selected period", example = "35000.00")
    private BigDecimal totalExpenses;

    @Schema(description = "Savings calculated as income minus expenses", example = "45000.00")
    private BigDecimal savings;

    @Schema(description = "Breakdown of expenses by category")
    private List<CategorySummaryResponse> categoryBreakdown;
}