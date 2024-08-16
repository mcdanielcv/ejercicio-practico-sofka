package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.configuracion.RabbitConfig;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.TransactionVo;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.AccountRepository;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.TransactionsRepository;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(@NonNull Long id) {
        return transactionsRepository.findById(id);
    }

    @Transactional
    public Transaction saveTransaction(TransactionVo transactionVo) throws ParseException {
        Optional<Account> accountOptional = accountRepository.findById(transactionVo.getAccountNumber());
        if (accountOptional.isPresent()) {
            Account accountDb = accountOptional.get();
            SimpleDateFormat formato = new SimpleDateFormat("dd/M/yyyy");
            Date dataFormateada = formato.parse(transactionVo.getTransactionDate());

            if (transactionVo.getValue() < 0 && accountDb.getAvailableBalance() < Math.abs(transactionVo.getValue())) {
                throw new RuntimeException("Saldo no disponible");
            }
            accountDb.setAvailableBalance(accountDb.getInitialBalance() + (1 * transactionVo.getValue()));

            Transaction transaction = new Transaction();
            transaction.setAccountNumber(transactionVo.getAccountNumber());
            transaction.setTypeMovement(transactionVo.getTypeMovement());
            transaction.setTransactionDate(dataFormateada);
            transaction.setValue(transactionVo.getValue());
            transaction.setBalance(accountDb.getAvailableBalance());
            Transaction savedMovimiento = transactionsRepository.save(transaction);
            accountRepository.save(accountDb);
            sendMessage(formato.format(savedMovimiento.getTransactionDate()));
            return savedMovimiento;
        } else {
            throw new RuntimeException("No existe la cuenta");
        }
    }

    @Transactional
    public Transaction deleteTransactionById(Long id) throws ParseException {
        Optional<Transaction> movimientoOptional = transactionsRepository.findById(id);
        if (movimientoOptional.isPresent()) {
            Optional<Account> cuentaOptional = accountRepository
                    .findById(movimientoOptional.get().getAccountNumber());
            if (cuentaOptional.isPresent()) {
                Account accountDb = cuentaOptional.get();
                accountDb.setAvailableBalance(accountDb.getInitialBalance() + ((-1) * movimientoOptional.get().getValue()));
                accountRepository.save(accountDb);
                transactionsRepository.deleteById(id);
                return movimientoOptional.get();
            } else {
                throw new RuntimeException("No Existe la cuenta para realizar el movimiento");
            }
        } else {
            throw new RuntimeException("No existe el movimiento a eliminar");
        }
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.MOVEMENT_CLIENT_QUEUE, message);
    }
}
