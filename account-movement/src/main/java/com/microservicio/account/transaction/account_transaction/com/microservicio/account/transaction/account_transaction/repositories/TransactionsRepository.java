package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.repositories;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT a from Transaction a  where a.accountNumber=?1 and a.transactionDate between ?2 and ?3")
    List<Transaction> findByCuentaIdAndFechaBetween(Long cuentaId, Date fechaInicio, Date fechaFin);
}
