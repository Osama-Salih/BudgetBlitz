package com.budget_blitz.category.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request object for creating a category")
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Schema(
            description = "Name of the category (must be unique per user)",
            example = "Food & Drinks",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 50
    )
    private String name;
}
