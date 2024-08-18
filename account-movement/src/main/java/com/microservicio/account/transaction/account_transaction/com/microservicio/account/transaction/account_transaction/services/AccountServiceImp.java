package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDTO;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDtoRed;
import jakarta.persistence.EntityNotFoundException;
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
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDTO> accountDTOS = accounts.stream().map(account -> AccountMapper.INSTANCE.AccountToDto(account)).collect(Collectors.toList());
        return accountDTOS;
    }

    @Transactional(readOnly = true)
    public Optional<Account> getById(@NonNull Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public AccountDTO saveAccount(AccountDTO accountDto) {
        if (accountRepository.existsAccountByAccountNumber(accountDto.getAccountNumber())) {
            throw new EntityNotFoundException("La Cuenta ya existe");
        }
        accountDto.setAvailableBalance(accountDto.getInitialBalance());
        Account account = AccountMapper.INSTANCE.dtoToAccount(accountDto);
        return AccountMapper.INSTANCE.AccountToDto(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO updateAccount(AccountDTO accountDto, Long id) {
        Optional<Account> cuentaOptional = accountRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            Account accountDb = cuentaOptional.get();
            accountDb.setAccountType(accountDto.getAccountType());
            accountDb.setInitialBalance(accountDto.getInitialBalance());
            accountDb.setState(accountDto.isState());
            return AccountMapper.INSTANCE.AccountToDto(accountRepository.save(accountDb));
        } else {
            throw new EntityNotFoundException("La account no existe");
        }
    }

    @Transactional
    public AccountDtoRed deleteAccountById(Long id) {
        Optional<Account> cuentaOptional = accountRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            accountRepository.deleteById(id);
            return AccountMapper.INSTANCE.AccountToDtoRed(cuentaOptional.get());
        } else {
            throw new EntityNotFoundException("La cuenta no existe");
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

    @Transactional(readOnly = true)
    public List<ReportDTO> generateReport(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/M/yyyy");

        List<Integer> idClients = clientApiService.getAllIdClients();
        List<Long> idClientsLong = idClients.stream().map(Integer::longValue).collect(Collectors.toList());

            return idClientsLong.stream().map(
                idClient -> {
                    List<Account> accounts = accountRepository.findClientByid(idClient);
                    return accounts.stream().map(account -> {
                        List<Transaction> movements = transactionsRepository.findByCuentaIdAndFechaBetween(account.getAccountNumber(),
                                startDate, endDate);
                        return movements.stream().map(movement -> {
                            ReportDTO reportDTO = new ReportDTO();
                            reportDTO.setNameClient(clientApiService.getNameClientById(idClient));
                            reportDTO.setTransactionDate(formato.format(movement.getTransactionDate()));
                            reportDTO.setAccountNumber(account.getAccountNumber().toString());
                            reportDTO.setType(account.getAccountType());
                            reportDTO.setInitialBalance(account.getInitialBalance());
                            reportDTO.setTransactionValue(movement.getValue());
                            reportDTO.setAvailableBalance(movement.getBalance());
                            return reportDTO;
                        }).collect(Collectors.toList());
                    }).flatMap(List::stream).collect(Collectors.toList());
                }).flatMap(List::stream).collect(Collectors.toList());
    }
}
