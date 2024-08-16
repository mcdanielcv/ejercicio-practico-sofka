package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.ReportDTO;
import org.springframework.lang.NonNull;


public interface AccountService {

    List<Account> getAllAccounts();

    Optional<Account> getById(@NonNull Long id);

    Account saveAccount(Account Account);

    Account updateAccount(Account Account, Long id);

    Account deleteAccountById(Long id);

    List<ReportDTO> generateReport(Long clientId, String startDate, String endDate)  throws ParseException;
  
}
