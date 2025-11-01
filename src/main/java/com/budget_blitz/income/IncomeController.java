package com.budget_blitz.income;

import com.budget_blitz.common.PageResponse;
import com.budget_blitz.income.request.AddIncomeRequest;
import com.budget_blitz.income.request.IncomeFilterRequest;
import com.budget_blitz.income.request.UpdateIncomeRequest;
import com.budget_blitz.income.response.IncomeResponse;
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
@RequestMapping("incomes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Tag(name = "Income Management", description = "Endpoints for managing user incomes (CRUD operations, filtering, sorting, and pagination).")
public class IncomeController {

    private final IncomeService incomeService;

    @Operation(
            summary = "Get all incomes",
            description = "Retrieve a paginated list of the authenticated user's incomes. Supports sorting and filtering by date, amount, and keyword.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of incomes retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping
    public ResponseEntity<PageResponse<IncomeResponse>> findAll(
            @Parameter(hidden = true) final Authentication principal,
            @Parameter(description = "Filter, pagination and sort options") @ModelAttribute IncomeFilterRequest filterRequest){
        return ResponseEntity.ok(this.incomeService.findAll(filterRequest, getUserId(principal)));
    }

    @Operation(
            summary = "Add new income",
            description = "Create a new income record for the authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddIncomeRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Income created successfully",
                            content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping
    public ResponseEntity<IncomeResponse> addIncome(
            @RequestBody
            @Valid
            final AddIncomeRequest request, @Parameter(hidden = true) final Authentication principal) {
        final IncomeResponse response = this.incomeService.addIncome(request, getUserId(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Get income by ID",
            description = "Retrieve a specific income record by its ID for the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Income retrieved successfully",
                            content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Income not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/{income_id}")
    public ResponseEntity<IncomeResponse> getIncomeById(
            @Parameter(description = "ID of the income to retrieve", example = "1")
            @PathVariable("income_id") final Integer incomeId,
            @Parameter(hidden = true) final Authentication principal) {
        return ResponseEntity.ok(this.incomeService.getIncomeById(incomeId, getUserId(principal)));
    }

    @Operation(
            summary = "Update income by ID",
            description = "Partially update an existing income record (amount, date, or description).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = UpdateIncomeRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Income updated successfully",
                            content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Income not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PatchMapping("/{income_id}")
    public ResponseEntity<IncomeResponse> updateIncomeById(
            @Parameter(description = "ID of the income to update", example = "1")
            @PathVariable("income_id") final Integer incomeId,
            @RequestBody @Valid final UpdateIncomeRequest request,
            @Parameter(hidden = true) final Authentication principal) {
        return ResponseEntity.ok(this.incomeService.updateIncomeById(request, incomeId, getUserId(principal)));
    }

    @Operation(
            summary = "Delete income by ID",
            description = "Delete an income record belonging to the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Income deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Income not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @DeleteMapping("/{income_id}")
    public ResponseEntity<Void> deleteIncomeById(
            @Parameter(description = "ID of the income to delete", example = "1")
            @PathVariable("income_id") final Integer incomeId,
            @Parameter(hidden = true) final Authentication principal) {
        this.incomeService.deleteIncomeById(incomeId, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    private Integer getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }
}