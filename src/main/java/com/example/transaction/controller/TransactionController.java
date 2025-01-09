package com.example.transaction.controller;

import com.example.transaction.dto.TransactionDTO;
import com.example.transaction.entity.Transaction;
import com.example.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionDTO>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TransactionDTO> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCustomerId(@PathVariable String customerId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCustomerId(customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountNumbers(@RequestParam List<String> accountNumbers) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountNumbers(accountNumbers);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/description")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDescription(@RequestParam String description) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDescription(description);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateDescription(@PathVariable Long id, @RequestParam String description, @RequestParam Integer version) {
        return ResponseEntity.ok(transactionService.updateTransactionDescription(id, description, version));
    }
}
