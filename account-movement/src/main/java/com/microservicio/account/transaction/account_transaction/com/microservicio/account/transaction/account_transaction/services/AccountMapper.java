package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Account;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDTO;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDtoRed;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "accountNumber", target = "accountNumber")
    Account dtoToAccount(AccountDTO accountDto);

    @Mapping(source = "accountNumber", target = "accountNumber")
    AccountDTO AccountToDto(Account account);

    @Mapping(source = "accountNumber", target = "accountNumber")
    AccountDtoRed AccountToDtoRed(Account account);
}
