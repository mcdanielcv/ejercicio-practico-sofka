package com.microservicio.cliente.persona.cliente_persona.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;

public interface ClientService {

    List<Client> getAllClients();

    Client saveClient(Client client);

    Client updateClient(Client client, Long id);

    Client deleteClientById(Long id);

    Optional<Client> getClientById(@NonNull Long id);

    String getNameClientById(Long clientId);

    List<Long> getAllIdClients();
}
