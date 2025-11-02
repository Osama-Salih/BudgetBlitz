package com.budget_blitz.category;

import com.budget_blitz.category.request.CategoryRequest;
import com.budget_blitz.category.response.CategoryResponse;
import com.budget_blitz.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public Category toCategory(CategoryRequest request, User user);

    public CategoryResponse toCategoryResponse(Category newCategory);
}
