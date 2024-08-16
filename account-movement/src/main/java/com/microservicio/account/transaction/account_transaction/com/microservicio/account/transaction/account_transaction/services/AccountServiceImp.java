package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.ReportDTO;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.AccountRepository;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories.TransactionsRepository;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private ClientApiService clientApiService;

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Account> getById(@NonNull Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public Account saveAccount(Account account) {
        if (accountRepository.existsAccountByAccountNumber(account.getAccountNumber())) {
            throw new RuntimeException("La Cuenta ya existe");
        }
        account.setAvailableBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    @Transactional
    public Account updateAccount(Account account, Long id) {
        Optional<Account> cuentaOptional = accountRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            Account accountDb = cuentaOptional.get();
            accountDb.setAccountType(account.getAccountType());
            accountDb.setInitialBalance(account.getInitialBalance());
            accountDb.setState(account.isState());
            return accountRepository.save(accountDb);
        } else {
            throw new RuntimeException("La account no existe");
        }
    }

    @Transactional
    public Account deleteAccountById(Long id) {
        Optional<Account> cuentaOptional = accountRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            accountRepository.deleteById(id);
            return cuentaOptional.get();
        } else {
            throw new RuntimeException("La cuenta no existe");
        }
    }

    @Transactional(readOnly = true)
    public List<ReportDTO> generateReport(Long clientId, String startDate, String endDate) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/M/yyyy");
        Date startDateVo = formato.parse(startDate);
        Date endDateVo = formato.parse(endDate);
        List<Account> accounts = accountRepository.findClientByid(clientId);
        return accounts.stream().map(account -> {
            List<Transaction> movements = transactionsRepository.findByCuentaIdAndFechaBetween(account.getAccountNumber(),
                    startDateVo, endDateVo);
            return movements.stream().map(movement -> {
                ReportDTO reportDTO = new ReportDTO();
                reportDTO.setNameClient(clientApiService.getNameClientById(clientId));
                reportDTO.setTransactionDate(formato.format(movement.getTransactionDate()));
                reportDTO.setAccountNumber(account.getAccountNumber().toString());
                reportDTO.setType(account.getAccountType());
                reportDTO.setInitialBalance(account.getInitialBalance());
                reportDTO.setTransactionValue(movement.getValue());
                reportDTO.setAvailableBalance(movement.getBalance());
                return reportDTO;
            }).collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());
    }
}
