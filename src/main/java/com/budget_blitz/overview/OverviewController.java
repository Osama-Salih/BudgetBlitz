package com.budget_blitz.overview;

import com.budget_blitz.overview.request.OverviewFilterRequest;
import com.budget_blitz.overview.response.OverviewResponse;
import com.budget_blitz.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("overview")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Tag(name = "Financial Overview", description = "Endpoints for viewing income, expenses, and savings summary")
public class OverviewController {

    private final OverviewService overviewService;

    @Operation(
            summary = "Get financial overview",
            description = "Returns total income, total expenses, savings, and expense breakdown by category. Supports filtering by month and year.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Overview retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OverviewResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<OverviewResponse> getOverview(
            @Parameter(hidden = true) @ModelAttribute final OverviewFilterRequest filterRequest,
            @Parameter(description = "Filter options, (month, year)") final Authentication principal) {
        return ResponseEntity.ok(this.overviewService.getOverview(filterRequest, getUserId(principal)));
    }

    private Integer getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }
}