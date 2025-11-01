package com.budget_blitz.income.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response object representing income details")
public class IncomeResponse {

    @Schema(
            description = "Unique identifier of the income record",
            example = "101"
    )
    private Integer id;

    @Schema(
            description = "Income amount",
            example = "2500.75"
    )
    private double amount;

    @Schema(
            description = "Date when the income was received",
            example = "2024-10-15"
    )
    private LocalDate date;

    @Schema(
            description = "Description or note about the income",
            example = "Freelance project payment"
    )
    private String description;
}
