package com.budget_blitz.income.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request object for filtering income records")
public class IncomeFilterRequest {

    @Schema(
            description = "Page number for pagination (0-based index)",
            example = "0",
            defaultValue = "0"
    )
    private int page = 0;
    @Schema(
            description = "Number of records per page",
            example = "10",
            defaultValue = "10"
    )
    private int size = 10;
    @Schema(
            description = "Sorting criteria in the format: field,order (e.g., createdDate,DESC)",
            example = "createdDate,ASC",
            defaultValue = "createdDate,DESC"
    )
    private String sort = "createdDate,DESC";
    @Schema(
            description = "Start date for filtering incomes (inclusive)",
            example = "2024-01-01"
    )
    private LocalDate fromDate;
    @Schema(
            description = "End date for filtering incomes (inclusive)",
            example = "2024-12-31"
    )
    private LocalDate toDate;
    @Schema(
            description = "Minimum income amount to filter",
            example = "100.0"
    )
    private Double minAmount;
    @Schema(
            description = "Maximum income amount to filter",
            example = "1000.0"
    )
    private Double maxAmount;
    @Schema(
            description = "Keyword to search within income descriptions",
            example = "bonus"
    )
    private String keyword;
}
