package com.budget_blitz.expense.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request object for updating an existing expense record")
public class UpdateExpenseRequest {

    @PositiveOrZero(message = "Amount must be at least 0.0")
    @Schema(
            description = "Optional amount (must be positive or zero)",
            example = "1500.00"
    )
    private BigDecimal amount;

    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Optional date in yyyy-MM-dd format (cannot be in the future)",
            example = "2025-10-30"
    )
    private LocalDate date;

    @Size(
            min = 2,
            max = 250,
            message = "Description must be between 2 to 250 character length"
    )
    @Schema(
            description = "Optional description for the expense entry",
            example = "House maintenance",
            minLength = 2,
            maxLength = 250
    )
    private String description;

    @Positive(message = "Category ID must be positive")
    @Schema(
            description = "The Optional unique ID of the category to which this expense belongs.",
            example = "3"
    )
    private Integer categoryId;
}
