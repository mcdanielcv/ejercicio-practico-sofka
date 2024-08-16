package com.microservicio.cliente.persona.cliente_persona.services;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservicio.cliente.persona.cliente_persona.configuration.RabbitConfig;
import com.microservicio.cliente.persona.cliente_persona.entities.Client;
import com.microservicio.cliente.persona.cliente_persona.repositories.ClientRepository;
import com.microservicio.cliente.persona.cliente_persona.services.EncryptService;

@Service
public class ClientServiceImp implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired(required = true)
    private EncryptService encryptService;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientById(@NonNull Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client saveClient(Client cliente) {
        cliente.setPassword(encryptService.encryptPassword(cliente.getPassword()));
        return clientRepository.save(cliente);
    }

    @Transactional
    public Client updateClient(Client client, Long id) {
        Optional<Client> clienteOptional = clientRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Client clientDb = clienteOptional.get();
            clientDb.setName(client.getName());
            clientDb.setAddress(client.getAddress());
            clientDb.setPhone(client.getPhone());
            clientDb.setPassword(client.getPassword());
            return clientRepository.save(clientDb);
        } else {
            throw new RuntimeException("El client no existe para actualizar");
        }
    }

    @Transactional
    public Client deleteClientById(Long id) {
        Optional<Client> clienteOptional = clientRepository.findById(id);
        if (clienteOptional.isPresent()) {
            clientRepository.deleteById(id);
            return clienteOptional.get();
        }else{
            throw new RuntimeException("El cliente no existe para eliminar");
        }
    }

    @Transactional(readOnly = true)
    public String getNameClientById(Long clienteId){
        Optional<Client> clienteOptional = clientRepository.findById(clienteId);
        if(clienteOptional.isPresent()){
            return clienteOptional.get().getName();
        }else{
            throw new RuntimeException("No existe cliente con el id: "+clienteId);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getAllIdClients(){
        return clientRepository.findAllIdClients();
    }

    @RabbitListener(queues = RabbitConfig.MOVEMENT_CLIENT_QUEUE)
    public void recibirMensaje(String mensaje) {
        System.out.println("Mensaje recibido desde cuenta-movimiento: " + mensaje);
        // Lógica para procesar el mensaje
    }
}
