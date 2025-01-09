package com.example.transaction.repository;

import com.example.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Paginated find all transactions
//    Page<Transaction> findAll(Pageable pageable);

    // Search by customer ID
    List<Transaction> findByCustomerId(String customerId);

    // Search by account number (can support multiple account numbers)
    List<Transaction> findByAccountNumberIn(List<String> accountNumbers);

    // Search by description with wildcard support
    @Query("SELECT t FROM Transaction t WHERE t.description LIKE %?1%")
    List<Transaction> findByDescriptionLike(String description);
}

