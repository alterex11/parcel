package com.mynt.exam.parcel.controller;

import com.mynt.exam.parcel.model.Parcel;
import com.mynt.exam.parcel.model.CostCalculationResult;
import com.mynt.exam.parcel.model.ParcelCostRule;
import com.mynt.exam.parcel.model.VoucherValidationResponse;
import com.mynt.exam.parcel.repository.ParcelCostRuleRepository;
import com.mynt.exam.parcel.service.ParcelCostCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/parcel")
public class ParcelController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ParcelCostRuleRepository parcelCostRuleRepository;

    @Autowired
    private ParcelCostCalculationService costCalculationService;


    @PostMapping("/calculate-cost")
    public ResponseEntity<CostCalculationResult> calculateCost(@RequestBody Parcel parcel) {
        CostCalculationResult result = costCalculationService.calculateCost(parcel);
        String parcelVoucherCode = parcel.getVoucherCode();

        if (parcelVoucherCode == null || parcelVoucherCode.equalsIgnoreCase("SKIPAPI") || parcelVoucherCode.equalsIgnoreCase("")) {

            result.setapicall("SKIP DISCOUNT VIA SKIPAPI VOUCHER OR BLANK VOUCHER");

        } else if (parcelVoucherCode.equalsIgnoreCase("MOCK50EXPIRE")) {

            VoucherValidationResponse mockResponse = new VoucherValidationResponse();
            mockResponse.setDiscount(50);
            mockResponse.setExpiry(LocalDate.of(2021, 12, 31)); // December 31, 2021

            if (isValidVoucher(mockResponse)) {
                result.setCost(applyVoucherDiscount(result.getCost(), mockResponse.getDiscount()));
            }
            result.setapicall("SKIP API MOCK API RESPONSE EXPIRE");
        } else if (parcelVoucherCode.equalsIgnoreCase("MOCK50")) {

            VoucherValidationResponse mockResponse = new VoucherValidationResponse();
            mockResponse.setDiscount(50);
            mockResponse.setExpiry(LocalDate.of(2025, 12, 31)); // December 31, 2025
            System.out.println("Applying: " + isValidVoucher(mockResponse));

            if (isValidVoucher(mockResponse)) {

                result.setCost(applyVoucherDiscount(result.getCost(), mockResponse.getDiscount()));

            }
            result.setapicall("SKIP API MOCK API RESPONSE NOT EXPIRE APPLIED 50%");
        }else {
            String apiKey = "apikey";
            String voucherApiUrl = "https://mynt-exam.mocklab.io/voucher/" +parcelVoucherCode + "?key=" + apiKey;

            try {
                VoucherValidationResponse response = restTemplate.getForObject(voucherApiUrl, VoucherValidationResponse.class);

                if (response != null && isValidVoucher(response)) {
                    result.setCost(applyVoucherDiscount(result.getCost(), response.getDiscount()));
                    result.setapicall("API CALL SUCCESS APPLIED DISCOUNT" + response.getDiscount() +'%');
                }
            } catch (ResourceAccessException e) {
                if (e.getCause() instanceof ConnectException) {
                    ConnectException connectException = (ConnectException) e.getCause();
                    String connectExceptionMessage = connectException.getMessage();
                    result.setCost(applyVoucherDiscount(result.getCost(), 0));
                    result.setapicall("Discount is set to 0 due to error refuse from api. Here is the error message" +connectExceptionMessage);
                } else {
                    result.setCost(applyVoucherDiscount(result.getCost(), 0));
                    result.setapicall("Discount is set to 0 due to error to connect in api.");
                }
            }
        }



        return ResponseEntity.ok(result);
    }


    private boolean isValidVoucher(VoucherValidationResponse response) {

        LocalDate expiryDate = response.getExpiry();
        LocalDate currentDate = LocalDate.now();
        return expiryDate != null && !expiryDate.isBefore(currentDate);
    }

    private double applyVoucherDiscount(double originalCost, int discountPercentage) {

        return originalCost * (1 - discountPercentage / 100.0);
    }



    @PostMapping("/calculate-cost-old")
    public ResponseEntity<CostCalculationResult> calculateCostold(@RequestBody Parcel parcel) {

        CostCalculationResult result = new CostCalculationResult();
        String parcelVoucherCode = parcel.getVoucherCode();

        if (parcelVoucherCode != null && parcelVoucherCode.equalsIgnoreCase("ZeroDelivery")) {
            result.setCost(0.0);
            result.setRemarks("Cost is Zero due to ZeroDelivery Special code.");
        } else {

            if (parcel.getWeight() > 50) {
                result.setCost(0.00);
                result.setRemarks("Rejected");
            } else if (parcel.getWeight() > 10) {
                result.setCost(20 * parcel.getWeight());
                result.setRemarks("Accepted, Heavy Parcel");
            } else if (parcel.getVolume() < 1500) {
                result.setCost(0.03 * parcel.getVolume());
                result.setRemarks("Accepted, Small Parcel");
            } else if (parcel.getVolume() < 2500) {
                result.setCost(0.04 * parcel.getVolume());
                result.setRemarks("Accepted, Medium Parcel");
            } else {
                result.setCost(0.05 * parcel.getVolume());
                result.setRemarks("Accepted, Large Parcel");
            }


            if (parcelVoucherCode == null || parcelVoucherCode.equalsIgnoreCase("SKIPAPI") || parcelVoucherCode.equalsIgnoreCase("")) {
                result.setapicall("SKIP DISCOUNT VIA SKIPAPI VOUCHER OR BLANK VOUCHER");

            } else {
                String apiKey = "apikey";
                String voucherApiUrl = "https://mynt-exam.mocklab.io/voucher/" +parcelVoucherCode + "?key=" + apiKey;

                try {
                    VoucherValidationResponse response = restTemplate.getForObject(voucherApiUrl, VoucherValidationResponse.class);

                    if (response != null && isValidVoucher(response)) {
                        result.setCost(applyVoucherDiscount(result.getCost(), response.getDiscount()));
                    }
                } catch (ResourceAccessException e) {
                    if (e.getCause() instanceof ConnectException) {
                        ConnectException connectException = (ConnectException) e.getCause();
                        String connectExceptionMessage = connectException.getMessage();
                        result.setCost(applyVoucherDiscount(result.getCost(), 0));
                        result.setapicall("Discount is set to 0 due to error refuse from api. Here is the error message" +connectExceptionMessage);
                    } else {
                        result.setCost(applyVoucherDiscount(result.getCost(), 0));
                        result.setapicall("Discount is set to 0 due to error to connect in api.");
                    }
                }
            }
        }

        return ResponseEntity.ok(result);
    }



}