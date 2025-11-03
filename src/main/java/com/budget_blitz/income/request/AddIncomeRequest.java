package com.budget_blitz.income.request;

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
@Schema(description = "Request body for adding a new income record")
public class AddIncomeRequest {

    @NotNull(message = "Amount required")
    @PositiveOrZero(message = "Amount must be at least 0.0")
    @Schema(
            description = "Income amount (must be positive or zero)",
            example = "1500.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal amount;

    @NotNull(message = "Date required")
    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Date of the income in yyyy-MM-dd format (cannot be in the future)",
            example = "2025-10-30",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate date;

    @Size(
            min = 2,
            max = 250,
            message = "Description must be between 2 to 250 character length"
    )
    @Schema(
            description = "Optional description for the income entry",
            example = "Freelance project payment",
            minLength = 2,
            maxLength = 250
    )
    private String description;
}
