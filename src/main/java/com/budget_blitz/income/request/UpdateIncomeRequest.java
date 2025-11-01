package com.budget_blitz.income.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request object for updating an existing income record")
public class UpdateIncomeRequest {

    @PositiveOrZero(message = "Amount must be at least 0.0")
    @Schema(
            description = "Updated income amount. Must be 0.0 or higher.",
            example = "2500.50"
    )
    private Double amount;

    @PastOrPresent(message = "Date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Updated income date. Cannot be a future date.",
            example = "2024-10-15"
    )
    private LocalDate date;

    @Size(
            min = 2,
            max = 250,
            message = "Description must be between 2 to 250 character length"
    )
    @Schema(
            description = "Updated description of the income. Must be between 2 and 250 characters.",
            example = "Monthly salary adjustment"
    )
    private String description;
}