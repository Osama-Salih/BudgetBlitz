package com.budget_blitz.expense.request;

import com.budget_blitz.common.FilterRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Request object for filtering expense records")
public class ExpenseFilterRequest extends FilterRequest {

    @Schema(
            description = "Category name to filter expenses grouped by category",
            example = "House"
    )
    private String category;
}
