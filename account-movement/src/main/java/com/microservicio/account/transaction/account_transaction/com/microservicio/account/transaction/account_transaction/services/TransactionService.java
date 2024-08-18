package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.TransactionDTO;

public interface TransactionService {

    public List<Transaction> getAllTransactions();

    public Optional<Transaction> getTransactionById(@NonNull Long id);

    public TransactionDTO saveTransaction(TransactionDTO transactionVo) throws ParseException;

    TransactionDTO deleteTransactionById(Long id) throws ParseException;

}
