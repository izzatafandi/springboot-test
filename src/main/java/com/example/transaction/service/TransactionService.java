package com.example.transaction.service;

import com.example.transaction.dto.TransactionDTO;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.repository.TransactionRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Paginated Retrieval
    public Page<TransactionDTO> getAllTransactions(int page, int size) {
        Page<Transaction> transactionPage = transactionRepository.findAll(PageRequest.of(page, size));
        return new PageImpl<>(TransactionMapper.toDTOList(transactionPage.getContent()));
    }

    // Search by customer ID
    public List<TransactionDTO> getTransactionsByCustomerId(String customerId) {
        return TransactionMapper.toDTOList(transactionRepository.findByCustomerId(customerId));
    }

    // Search by one or more account numbers
    public List<TransactionDTO> getTransactionsByAccountNumbers(List<String> accountNumbers) {
        return TransactionMapper.toDTOList(transactionRepository.findByAccountNumberIn(accountNumbers));
    }

    // Search by description with wildcard
    public List<TransactionDTO> getTransactionsByDescription(String description) {
        return TransactionMapper.toDTOList(transactionRepository.findByDescriptionLike(description));
    }

    // Update TransactionDTO description with optimistic locking
    public TransactionDTO updateTransactionDescription(Long id, String description, Integer version) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        if (!transaction.getVersion().equals(version)) {
            throw new OptimisticLockException("Conflict: Transaction has been updated by another process");
        }
        transaction.setDescription(description);
        return TransactionMapper.toDTO(transactionRepository.save(transaction));
    }
}
