package com.budget_blitz.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("""
            SELECT COUNT(c) > 0
            FROM Category c
            WHERE c.user.id =:userId
            AND LOWER(c.name) = LOWER(:name)
           """)
    boolean categoryExistsByNameAndUserIdIgnoreCase(@Param("name") String name, @Param("userId") Integer userId);

    @Query("""
            SELECT c
            FROM Category c
            WHERE c.user.id =:userId
            """)
    Page<Category> findAllCategoriesByUserId(Pageable pageable, @Param("userId") Integer userId);
}
