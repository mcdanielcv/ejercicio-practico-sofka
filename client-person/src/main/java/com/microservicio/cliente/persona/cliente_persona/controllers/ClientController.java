package com.microservicio.cliente.persona.cliente_persona.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;
import com.microservicio.cliente.persona.cliente_persona.models.ResponseVo;
import com.microservicio.cliente.persona.cliente_persona.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/clients")
@Validated
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<?> getAllClients() {
        List<Client> list = clientService.getAllClients();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseVo(false, "No existen datos"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseVo(true, "", list));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveClient(@Valid @RequestBody Client client, BindingResult result) {
        try {
            if (result.hasErrors())
                return validationData(result);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseVo(true, "Cliente registrado", clientService.saveClient(client)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClientById(@Valid @RequestBody Client client, BindingResult result,
                                               @PathVariable Long id) {
        try {
            if (result.hasErrors()) {
                return validationData(result);
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseVo(true, "Cliente Actualizado", clientService.updateClient(client, id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseVo(true, "Cliente Borrado", clientService.deleteClientById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getNameClientById(@PathVariable Long clientId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(clientService.getNameClientById(clientId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseVo(false, e.getMessage()));
        }
    }

    @GetMapping("/client/")
    public List<Long> getAllIdClients() {
        return  clientService.getAllIdClients();
    }

    private ResponseEntity<?> validationData(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
