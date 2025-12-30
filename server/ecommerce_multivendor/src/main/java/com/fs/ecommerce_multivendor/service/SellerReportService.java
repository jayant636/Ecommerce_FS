package com.fs.ecommerce_multivendor.service;

import com.fs.ecommerce_multivendor.entity.Seller;
import com.fs.ecommerce_multivendor.entity.SellerReports;

public interface SellerReportService {
    SellerReports getSellerReport(Seller seller);
    SellerReports updateSellerReport(SellerReports sellerReports);
}
