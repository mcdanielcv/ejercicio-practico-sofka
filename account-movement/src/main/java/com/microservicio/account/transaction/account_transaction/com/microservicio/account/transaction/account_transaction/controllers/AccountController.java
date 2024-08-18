package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.controllers;

import java.util.List;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.models.ResponseVo;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services.AccountService;
import com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.services.UtilitariosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    UtilitariosService utilitariosService;

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        List<AccountDTO> list = accountService.getAllAccounts();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVo(false, "No existen datos"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseVo(true, "", list));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveAccount(@Valid @RequestBody AccountDTO accountDto, BindingResult result) {
        try {
            if (result.hasErrors())
                return utilitariosService.validacionDatos(result);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseVo(true, "Cuenta creada", accountService.saveAccount(accountDto)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountById(@Valid @RequestBody AccountDTO accountDto, BindingResult result,
                                               @PathVariable Long id) {
        try {
            if (result.hasErrors()) {
                return utilitariosService.validacionDatos(result);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseVo(true, "Cuenta Actualizado", accountService.updateAccount(accountDto, id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseVo(true, "Cuenta Borrada", accountService.deleteAccountById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }
}
