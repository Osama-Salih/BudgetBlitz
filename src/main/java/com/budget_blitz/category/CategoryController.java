package com.budget_blitz.category;

import com.budget_blitz.category.request.CategoryRequest;
import com.budget_blitz.category.response.CategoryResponse;
import com.budget_blitz.common.PageResponse;
import com.budget_blitz.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Tag(name = "Categories", description = "Endpoints for managing user-defined categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get all categories for the logged-in user",
            description = "Retrieves paginated categories created by the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> findAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,

            @Parameter(hidden = true) final Authentication principal) {
        return ResponseEntity.ok(this.categoryService.findAll(page, size, getUserId(principal)));
    }

    @Operation(
            summary = "Create a new category",
            description = "Allows the authenticated user to create a new category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Category with the same name already exists", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
            @RequestBody @Valid final CategoryRequest request,
            @Parameter(hidden = true) final Authentication principal){
        final CategoryResponse response = this.categoryService.addCategory(request, getUserId(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Delete a category by ID",
            description = "Deletes a category belonging to the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
            }
    )
    @DeleteMapping("/{category_id}")
    public ResponseEntity<Void> deleteCategoryById(
            @Parameter(description = "ID of the category to delete", example = "1")
            @PathVariable("category_id") final Integer categoryId,
            @Parameter(hidden = true) final Authentication principal){
        this.categoryService.deleteCategoryById(categoryId, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    private Integer getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }

}
