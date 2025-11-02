package com.budget_blitz.category;

import com.budget_blitz.category.request.CategoryRequest;
import com.budget_blitz.category.response.CategoryResponse;
import com.budget_blitz.common.PageResponse;

public interface CategoryService {

    CategoryResponse addCategory(final CategoryRequest request, final Integer userId);
    PageResponse<CategoryResponse> findAll(final int page, final int size, final Integer userId);
    void deleteCategoryById(final Integer categoryId, final Integer userId);
}
