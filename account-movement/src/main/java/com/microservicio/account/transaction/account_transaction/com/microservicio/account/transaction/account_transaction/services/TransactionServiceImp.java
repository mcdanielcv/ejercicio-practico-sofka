package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.TransactionDTO;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.AccountRepository;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.TransactionsRepository;

@Service
public class TransactionServiceImp implements TransactionService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private AccountRepository accountRepository;

   @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(@NonNull Long id) {
        return transactionsRepository.findById(id);
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDto) throws ParseException {
        Optional<Account> accountOptional = accountRepository.findById(transactionDto.getAccountNumber());
        if (accountOptional.isPresent()) {
            Account accountDb = accountOptional.get();

            if (transactionDto.getValue() < 0 && accountDb.getAvailableBalance() < Math.abs(transactionDto.getValue())) {
                throw new RuntimeException("Saldo no disponible");
            }
            accountDb.setAvailableBalance(accountDb.getInitialBalance() + (1 * transactionDto.getValue()));
            Transaction transaction = TransactionMapper.INSTANCE.dtoToTransaction(transactionDto);
            transaction.setBalance(accountDb.getAvailableBalance());
            Transaction savedMovimiento = transactionsRepository.save(transaction);
            accountRepository.save(accountDb);
            return TransactionMapper.INSTANCE.transactionToDto(savedMovimiento);
        } else {
            throw new EntityNotFoundException("No existe la cuenta");
        }
    }

    @Transactional
    public TransactionDTO deleteTransactionById(Long id) throws ParseException {
        Optional<Transaction> movimientoOptional = transactionsRepository.findById(id);
        if (movimientoOptional.isPresent()) {
            Optional<Account> cuentaOptional = accountRepository
                    .findById(movimientoOptional.get().getAccountNumber());
            if (cuentaOptional.isPresent()) {
                Account accountDb = cuentaOptional.get();
                accountDb.setAvailableBalance(accountDb.getInitialBalance() + ((-1) * movimientoOptional.get().getValue()));
                accountRepository.save(accountDb);
                transactionsRepository.deleteById(id);
                return TransactionMapper.INSTANCE.transactionToDto(movimientoOptional.get());
            } else {
                throw new EntityNotFoundException("No Existe la cuenta para realizar el movimiento");
            }
        } else {
            throw new EntityNotFoundException("No existe el movimiento a eliminar");
        }
    }
}
