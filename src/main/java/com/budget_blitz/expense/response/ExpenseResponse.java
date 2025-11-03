package com.budget_blitz.expense.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response object representing expense details")
public class ExpenseResponse {

    @Schema(
            description = "Unique identifier of the expense record",
            example = "101"
    )
    private Integer id;

    @Schema(
            description = "Expense amount",
            example = "2500.75"
    )
    private BigDecimal amount;

    @Schema(
            description = "Date on which expense were paid",
            example = "2024-10-15"
    )
    private LocalDate date;

    @Schema(
            description = "Description about the expense",
            example = "House rent paid"
    )
    private String description;

    @Schema(
            description = "Category name this expense belong to",
            example = "House"
    )
    private String categoryName;
}