package com.mynt.exam.parcel.repository;

import com.mynt.exam.parcel.model.ParcelCostRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParcelCostRuleRepository extends JpaRepository<ParcelCostRule, Long> {
    List<ParcelCostRule> findAll();
}
