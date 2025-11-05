package com.budget_blitz.overview.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Filter options for financial overview")
public class OverviewFilterRequest {

    @Schema(description = "Filter by month (1–12)", example = "11")
    private Integer month;

    @Schema(description = "Filter by year", example = "2025")
    private Integer year;
}
