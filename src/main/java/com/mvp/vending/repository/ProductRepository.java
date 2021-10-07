package com.mvp.vending.repository;

import com.mvp.vending.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by Majied on 05/10/2021.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Find products in a vending machine
     *
     * @param name the vending machine name
     * @return if the machine exists
     */
    Collection<Product> findByMachineName(String name);
}
