package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.TransactionVo;

public interface TransactionService {

    public List<Transaction> getAllTransactions();

    public Optional<Transaction> getTransactionById(@NonNull Long id);

    public Transaction saveTransaction(TransactionVo transactionVo) throws ParseException;

    Transaction deleteTransactionById(Long id) throws ParseException;

    public void sendMessage(String message) ;
}
