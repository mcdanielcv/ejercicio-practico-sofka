package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDTO;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDtoRed;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.ReportDTO;
import org.springframework.lang.NonNull;


public interface AccountService {

    List<AccountDTO> getAllAccounts();

    Optional<Account> getById(@NonNull Long id);

    AccountDTO saveAccount(AccountDTO accountDto);

    AccountDTO updateAccount(AccountDTO accountDto, Long id);

    AccountDtoRed deleteAccountById(Long id);

    List<ReportDTO> generateReport(Long clientId, String startDate, String endDate)  throws ParseException;

    List<ReportDTO> generateReport(Date startDate, Date endDate) throws ParseException;
  
}
