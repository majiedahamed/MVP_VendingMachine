package com.mvp.vending.repository;

import com.mvp.vending.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by Majied on 05/10/2021.
 */
public interface CoinRepository extends JpaRepository<Coin, Long> {
    /**
     * Find all coins in a given machine
     *
     * @param name
     * @return
     */
    Collection<Coin> findByMachineName(String name);
}
