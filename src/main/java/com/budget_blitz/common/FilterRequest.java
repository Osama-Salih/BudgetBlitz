package com.budget_blitz.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class FilterRequest {

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
            description = "Start date for filtering (inclusive)",
            example = "2024-01-01"
    )
    private LocalDate fromDate;
    @Schema(
            description = "End date for filtering (inclusive)",
            example = "2024-12-31"
    )
    private LocalDate toDate;
    @Schema(
            description = "Minimum amount to filter",
            example = "100.0"
    )
    private BigDecimal minAmount;
    @Schema(
            description = "Maximum amount to filter",
            example = "1000.0"
    )
    private BigDecimal maxAmount;
    @Schema(
            description = "Keyword to search within descriptions",
            example = "Maintenance"
    )
    private String keyword;
}
