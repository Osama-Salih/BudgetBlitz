package com.budget_blitz.expense;

import com.budget_blitz.common.PageResponse;
import com.budget_blitz.expense.request.AddExpenseRequest;
import com.budget_blitz.expense.request.ExpenseFilterRequest;
import com.budget_blitz.expense.request.UpdateExpenseRequest;
import com.budget_blitz.expense.response.ExpenseResponse;
import com.budget_blitz.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("expenses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Tag(name = "Expense Management", description = "Endpoints for managing user expenses (CRUD operations, filtering, sorting, and pagination).")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "Get all expenses",
            description = "Retrieves a paginated list of user expenses with optional filters (date range, amount, category, etc.)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<PageResponse<ExpenseResponse>> findAll(
            @Parameter(hidden = true) final Authentication principal,
            @Parameter(description = "Filter, pagination and sort options") @ModelAttribute ExpenseFilterRequest filterRequest){
        return ResponseEntity.ok(this.expenseService.findAll(filterRequest, getUserId(principal)));
    }

    @Operation(
            summary = "Create a new expense",
            description = "Creates a new expense record for the authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Expense details (amount, date, description, categoryId)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddExpenseRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Expense created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(
            @RequestBody @Valid final AddExpenseRequest request,
            @Parameter(hidden = true) final Authentication principal) {
        final ExpenseResponse response = this.expenseService.addExpense(request, getUserId(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get expense by ID",
            description = "Fetches a specific expense by its ID for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Expense not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
    })
    @GetMapping("/{expense_id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @Parameter(description = "ID of the expense to retrieve", example = "1")
            @PathVariable("expense_id") final Integer expenseId,
            @Parameter(hidden = true) final Authentication principal) {
        return ResponseEntity.ok(this.expenseService.getExpenseById(expenseId, getUserId(principal)));
    }

    @Operation(
            summary = "Update an expense",
            description = "Updates an existing expense record by ID for the authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated expense details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateExpenseRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expense updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Expense not found", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            }
    )
    @PatchMapping("/{expense_id}")
    public ResponseEntity<ExpenseResponse> updateExpenseById(
            @Parameter(description = "ID of the expense to update", example = "1")
            @PathVariable("expense_id") final Integer expenseId,
            @RequestBody @Valid final UpdateExpenseRequest request,
            @Parameter(hidden = true) final Authentication principal) {
        return ResponseEntity.ok(this.expenseService.updateExpenseById(request, expenseId, getUserId(principal)));
    }

    @Operation(
            summary = "Delete an expense",
            description = "Deletes an expense by its ID for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Expense deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
    })
    @DeleteMapping("/{expense_id}")
    public ResponseEntity<Void> deleteExpenseById(
            @Parameter(description = "ID of the expense to delete", example = "1")
            @PathVariable("expense_id") final Integer expenseId,
            @Parameter(hidden = true) final Authentication principal) {
        this.expenseService.deleteExpenseById(expenseId, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    private Integer getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }
}