package com.microservicio.cliente.persona.cliente_persona.services;

import java.util.List;
import java.util.Optional;

import com.microservicio.cliente.persona.cliente_persona.models.ClientDTO;
import org.springframework.lang.NonNull;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;

public interface ClientService {

    List<ClientDTO> getAllClients();

    ClientDTO saveClient(Client client);

    ClientDTO updateClient(Client client, Long id);

    ClientDTO deleteClientById(Long id);

    Optional<Client> getClientById(@NonNull Long id);

    String getNameClientById(Long clientId);

    List<Long> getAllIdClients();
}
