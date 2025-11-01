package com.budget_blitz.income;

import com.budget_blitz.income.request.AddIncomeRequest;
import com.budget_blitz.income.request.UpdateIncomeRequest;
import com.budget_blitz.income.response.IncomeResponse;
import com.budget_blitz.users.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    public Income toIncome(AddIncomeRequest request, User user);

    public IncomeResponse toIncomeResponse(Income income);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateIncomeRequestToIncome(UpdateIncomeRequest request, @MappingTarget Income income);
}