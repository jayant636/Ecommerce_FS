package com.fs.ecommerce_multivendor.repository;

import com.fs.ecommerce_multivendor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

   User findByEmail(String email);

}
