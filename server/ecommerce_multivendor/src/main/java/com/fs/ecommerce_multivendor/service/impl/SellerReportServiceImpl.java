package com.fs.ecommerce_multivendor.service.impl;

import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.entity.SellerReports;
import com.fs.ecommerce_multivendor.repository.SellerReportRepository;
import com.fs.ecommerce_multivendor.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {


    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReports getSellerReport(Seller seller) {
        SellerReports sellerReports = sellerReportRepository.findBySellerId(seller.getId());
        if(sellerReports == null){
            SellerReports newSellerReport = new SellerReports();
            newSellerReport.setSeller(seller);
            return sellerReportRepository.save(newSellerReport);
        }
        return sellerReports;
    }

    @Override
    public SellerReports updateSellerReport(SellerReports sellerReports) {

        return sellerReportRepository.save(sellerReports);
    }
}
