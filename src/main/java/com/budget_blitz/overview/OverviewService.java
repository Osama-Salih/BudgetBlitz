package com.budget_blitz.overview;

import com.budget_blitz.overview.request.OverviewFilterRequest;
import com.budget_blitz.overview.response.OverviewResponse;

public interface OverviewService {
    OverviewResponse getOverview(final OverviewFilterRequest filterRequest, final Integer userId);
}
