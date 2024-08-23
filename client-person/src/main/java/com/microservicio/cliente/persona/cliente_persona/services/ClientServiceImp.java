package com.microservicio.cliente.persona.cliente_persona.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.microservicio.cliente.persona.cliente_persona.models.ClientDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;
import com.microservicio.cliente.persona.cliente_persona.repositories.ClientRepository;

@Service
public class ClientServiceImp implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired(required = true)
    private EncryptService encryptService;

    @Autowired
    private ClientProducerService clientProducerService;


    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        List<Client> clients= clientRepository.findAll();
        List<ClientDTO> clientDTOS = clients.stream().map(client-> ClientMapper.INSTANCE.ClientToDto(client)).collect(Collectors.toList());
        return clientDTOS;
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientById(@NonNull Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public ClientDTO saveClient(Client cliente) {
        cliente.setPassword(encryptService.encryptPassword(cliente.getPassword()));
        ClientDTO clientDTO = ClientMapper.INSTANCE.ClientToDto(clientRepository.save(cliente));
        clientProducerService.sendClientMessage(clientDTO.getClientId());
        return clientDTO;
    }

    @Transactional
    public ClientDTO updateClient(Client client, Long id) {
        Optional<Client> clienteOptional = clientRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Client clientDb = clienteOptional.get();
            clientDb.setName(client.getName());
            clientDb.setAddress(client.getAddress());
            clientDb.setPhone(client.getPhone());
            clientDb.setPassword(encryptService.encryptPassword(client.getPassword()));
            return ClientMapper.INSTANCE.ClientToDto(clientRepository.save(clientDb));
        } else {
            throw new EntityNotFoundException("El client no existe para actualizar");
        }
    }

    @Transactional
    public ClientDTO deleteClientById(Long id) {
        Optional<Client> clienteOptional = clientRepository.findById(id);
        if (clienteOptional.isPresent()) {
            clientRepository.deleteById(id);
            return ClientMapper.INSTANCE.ClientToDto(clienteOptional.get());
        }else{
            throw new EntityNotFoundException("El cliente no existe para eliminar");
        }
    }

    @Transactional(readOnly = true)
    public String getNameClientById(Long clienteId){
        Optional<Client> clienteOptional = clientRepository.findById(clienteId);
        if(clienteOptional.isPresent()){
            return clienteOptional.get().getName();
        }else{
            throw new EntityNotFoundException("No existe cliente con el id: "+clienteId);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getAllIdClients(){
        List<Integer> idInteger = clientRepository.findAllIdClients();
         List<Long> idsAsLong = idInteger.stream()
                        .map(Number::longValue)  // Convierte a Long
                        .collect(Collectors.toList());
        return idsAsLong;
    }


}
