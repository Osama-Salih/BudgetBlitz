package com.budget_blitz.category.impl;

import com.budget_blitz.category.Category;
import com.budget_blitz.category.CategoryMapper;
import com.budget_blitz.category.CategoryRepository;
import com.budget_blitz.category.CategoryService;
import com.budget_blitz.category.request.CategoryRequest;
import com.budget_blitz.category.response.CategoryResponse;
import com.budget_blitz.common.PageResponse;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.users.User;
import com.budget_blitz.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse<CategoryResponse> findAll(final int page, final int size, final Integer userId) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        final Page<Category> categories = this.categoryRepository.findAllCategoriesByUserId(pageable, userId);
        final List<CategoryResponse> responses = categories.stream().map(categoryMapper::toCategoryResponse).toList();

        return new PageResponse<>(
                responses,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isFirst(),
                categories.isLast()
        );
    }

    @Override
    @Transactional
    public CategoryResponse addCategory(final CategoryRequest request, final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        final boolean category = this.categoryRepository.categoryExistsByNameAndUserIdIgnoreCase(request.getName(), user.getId());
        if (category) {
            throw new BusinessException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        final Category newCategory = this.categoryMapper.toCategory(request, user);
        this.categoryRepository.save(newCategory);
        log.info("Saving category: {}", newCategory);
        return this.categoryMapper.toCategoryResponse(newCategory);
    }

    @Override
    public void deleteCategoryById(final Integer categoryId, final Integer userId) {
            final Category category = this.categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if(!Objects.equals(category.getUser().getId(), userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        log.info("Deleting category with id={} for user={}", categoryId, userId);
        this.categoryRepository.deleteById(categoryId);
    }
}
