package com.budget_blitz.overview.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Summary of expenses by category")
public class CategorySummaryResponse {

    @Schema(description = "Name of the category", example = "House")
    private String categoryName;

    @Schema(description = "Total amount spent in this category", example = "22000.00")
    private BigDecimal total;
}