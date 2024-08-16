package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionVo {

    private Long accountNumber;

    private String transactionDate;

    private String typeMovement;

    private double value;

    public TransactionVo(Long accountNumber, String transactionDate, String typeMovement, double value) {
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.typeMovement = typeMovement;
        this.value = value;
    }
}
