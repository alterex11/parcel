package com.mynt.exam.parcel.service;

import com.mynt.exam.parcel.model.Parcel;
import com.mynt.exam.parcel.model.ParcelCostRule;
import com.mynt.exam.parcel.repository.ParcelCostRuleRepository;
import com.mynt.exam.parcel.model.CostCalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelCostCalculationService {

    @Autowired
    private ParcelCostRuleRepository ruleRepository;

    public CostCalculationResult calculateCost(Parcel parcel) {
        CostCalculationResult result = new CostCalculationResult();
        List<ParcelCostRule> rules = ruleRepository.findAll();

        for (ParcelCostRule rule : rules) {
            if (isRuleApplicable(rule, parcel)) {
                double calculatedCost = calculateRuleCost(rule, parcel);
                result.setCost(calculatedCost);
                result.setRemarks(rule.getRemarks());
                break; // Apply the first matching rule
            }
        }

        return result;
    }

    private boolean isRuleApplicable(ParcelCostRule rule, Parcel parcel) {
        String conditionType = rule.getConditionType();
        double conditionValue = rule.getConditionValue();

        if (conditionType == null || conditionType.trim().isEmpty()) {
            return true; // Always true if conditionType is blank
        } else if ("weight".equalsIgnoreCase(conditionType)) {
            return parcel.getWeight() > conditionValue;
        } else if ("volume".equalsIgnoreCase(conditionType)) {
            return parcel.getVolume() < conditionValue;
        }

        return false;
    }

    private double calculateRuleCost(ParcelCostRule rule, Parcel parcel) {
        String conditionType = rule.getConditionType();
        double multiplier = rule.getCostMultiplier();
        String multiplierType = rule.getMultiplierType();
        double parcelValue = multiplierType.equalsIgnoreCase("volume") ? parcel.getVolume() : parcel.getWeight();
        if ("weight".equalsIgnoreCase(conditionType)) {
            return multiplier * parcelValue;
        } else if ("volume".equalsIgnoreCase(conditionType)) {
            return multiplier * parcelValue;
        }else if ("".equalsIgnoreCase(conditionType)) {
            return multiplier * parcelValue;
        }

        return 0.0;
    }
}
