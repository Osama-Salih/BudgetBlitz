package com.budget_blitz.category.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response object representing category details")
public class CategoryResponse {

    @Schema(
            description = "Unique identifier of the category record",
            example = "101"
    )
    private Integer id;

    @Schema(
            description = "Name of the category (must be unique per user)",
            example = "Food & Drinks"
    )
    private String name;
}
