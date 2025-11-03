package com.budget_blitz.expense;


import com.budget_blitz.category.Category;
import com.budget_blitz.expense.request.AddExpenseRequest;
import com.budget_blitz.expense.request.UpdateExpenseRequest;
import com.budget_blitz.expense.response.ExpenseResponse;
import com.budget_blitz.users.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public Expense toExpense(AddExpenseRequest request, User user, Category category);

    @Mapping(target = "categoryName", source = "category.name")
    public ExpenseResponse toExpenseResponse(Expense createdExpense);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateExpenseRequestToExpense(UpdateExpenseRequest request, @MappingTarget Expense expense);
}