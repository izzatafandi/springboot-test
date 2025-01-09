package com.example.transaction.mapper;

import com.example.transaction.dto.TransactionDTO;
import com.example.transaction.entity.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setAccountNumber(dto.getAccountNumber());
        transaction.setTrxAmount(dto.getTrxAmount());
        transaction.setDescription(dto.getDescription());

        if (dto.getTrxDate() != null) {
            transaction.setTrxDate(LocalDate.parse(dto.getTrxDate(), DATE_FORMATTER));
        }

        if (dto.getTrxTime() != null) {
            transaction.setTrxTime(LocalTime.parse(dto.getTrxTime(), TIME_FORMATTER));
        }

        transaction.setCustomerId(dto.getCustomerId());
        return transaction;
    }

    public static TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAccountNumber(transaction.getAccountNumber());
        dto.setTrxAmount(transaction.getTrxAmount());
        dto.setDescription(transaction.getDescription());

        if (transaction.getTrxDate() != null) {
            dto.setTrxDate(transaction.getTrxDate().format(DATE_FORMATTER));
        }

        if (transaction.getTrxTime() != null) {
            dto.setTrxTime(transaction.getTrxTime().format(TIME_FORMATTER));
        }

        dto.setCustomerId(transaction.getCustomerId());
        return dto;
    }

    public static List<Transaction> toEntityList(List<TransactionDTO> dtoList) {
        List<Transaction> entityList = new ArrayList<>();

        for (TransactionDTO dto : dtoList) {
            entityList.add(toEntity(dto));
        }

        return entityList;
    }

    public static List<TransactionDTO> toDTOList(List<Transaction> transactionList) {
        List<TransactionDTO> dtoList = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            dtoList.add(toDTO(transaction));
        }

        return dtoList;
    }
}
