package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.controllers;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.entities.Transaction;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.TransactionVo;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.ResponseVo;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services.TransactionService;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services.UtilitariosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UtilitariosService utilitariosService;

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<Transaction> list = transactionService.getAllTransactions();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVo(false, "No existen datos"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseVo(true, "", list));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveTransaction(@Valid @RequestBody TransactionVo transactionVo, BindingResult result) {
        try {
            if (result.hasErrors())
                return utilitariosService.validacionDatos(result);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseVo(true, "Movimiento registrado", transactionService.saveTransaction(transactionVo)));
        } catch (RuntimeException | ParseException e) {
            System.out.println("error-<" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionByNumTransaccion(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseVo(true, "Movimiento Eliminado", transactionService.deleteTransactionById(id)));
        } catch (RuntimeException | ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @GetMapping("/sendMessageClient")
    public String sendMessageClient(@RequestParam String message) {
        transactionService.sendMessage(message);
        return "Mensaje enviado a cliente-persona: " + message;
    }
}
