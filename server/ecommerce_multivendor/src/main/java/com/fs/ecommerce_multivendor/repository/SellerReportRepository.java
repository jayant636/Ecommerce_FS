package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.SellerReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReports,Long> {
    SellerReports findBySellerId(Long sellerId);
}
