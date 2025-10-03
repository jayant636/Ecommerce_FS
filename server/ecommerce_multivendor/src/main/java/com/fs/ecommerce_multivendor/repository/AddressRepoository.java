package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepoository extends JpaRepository<Address,Long> {
}
