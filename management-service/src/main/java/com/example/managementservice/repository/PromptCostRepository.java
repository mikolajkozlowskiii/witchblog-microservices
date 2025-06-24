package com.example.managementservice.repository;

import com.example.managementservice.model.PromptCost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PromptCostRepository extends CrudRepository<PromptCost, Long> {
    @Query("SELECT COALESCE(SUM(p.usedTokens), 0) FROM PromptCost p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Long sumUsedTokensBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
