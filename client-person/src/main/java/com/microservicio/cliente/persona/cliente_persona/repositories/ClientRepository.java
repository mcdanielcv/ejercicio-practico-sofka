package com.microservicio.cliente.persona.cliente_persona.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long>{

    @Query(value = "SELECT a.client_Id FROM Client a", nativeQuery = true)
    List<Integer> findAllIdClients();



}
