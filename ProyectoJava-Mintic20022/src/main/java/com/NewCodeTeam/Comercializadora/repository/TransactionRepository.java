package com.NewCodeTeam.Comercializadora.repository;


import com.NewCodeTeam.Comercializadora.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
