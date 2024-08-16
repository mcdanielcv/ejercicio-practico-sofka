package com.microservicio.cliente.persona.cliente_persona.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservicio.cliente.persona.cliente_persona.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long>{
    /*
    @Query("SELECT c FROM Cliente c WHERE c.identification = :identification")
    List<Client> findByIdentification(@Param("identification") String identification);
     */

    @Query(value = "SELECT a.clientId from Client a")
    List<Long> findAllIdClients();
}
