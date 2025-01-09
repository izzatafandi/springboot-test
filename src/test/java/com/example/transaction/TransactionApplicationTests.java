package com.example.transaction;

import com.example.transaction.dto.TransactionDTO;
import com.example.transaction.entity.Transaction;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionApplicationTests {

	@MockitoBean
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionService transactionService;

	private Transaction transaction;
	private int page, size;

	@BeforeEach
	public void setUp() {
		transaction = new Transaction();
		transaction.setId(99L);
		transaction.setAccountNumber("12345");
		transaction.setDescription("Unit Testing");
		transaction.setTrxAmount(100.50);
		transaction.setCustomerId("999");

		page = 0;
		size = 10;
	}

	@Test
	public void testGetAllTransactions() {
		when(transactionRepository.findAll(PageRequest.of(page, size))).thenReturn(new PageImpl<>(List.of(transaction)));
		Page<TransactionDTO> transactionDTOPage = transactionService.getAllTransactions(page, size);
		verify(transactionRepository, times(1)).findAll(PageRequest.of(page, size));
	}

	@Test
	public void testGetTransactionsByCustomerId() {
		when(transactionRepository.findByCustomerId("999")).thenReturn(List.of(transaction));
		List<TransactionDTO> transactionDTOList = transactionService.getTransactionsByCustomerId("999");
		verify(transactionRepository, times(1)).findByCustomerId("999");
	}

	@Test
	public void testGetTransactionsByAccountNumbers() {
		when(transactionRepository.findByAccountNumberIn(new ArrayList<>(List.of("12345")))).thenReturn(List.of(transaction));
		List<TransactionDTO> transactionDTOList = transactionService.getTransactionsByAccountNumbers(new ArrayList<>(List.of("12345")));
		verify(transactionRepository, times(1)).findByAccountNumberIn(new ArrayList<>(List.of("12345")));
	}

	@Test
	public void testGetTransactionsByDescription() {
		when(transactionRepository.findByDescriptionLike("test")).thenReturn(List.of(transaction));
		List<TransactionDTO> transactionDTOList = transactionService.getTransactionsByDescription("test");
		verify(transactionRepository, times(1)).findByDescriptionLike("test");
	}

}
