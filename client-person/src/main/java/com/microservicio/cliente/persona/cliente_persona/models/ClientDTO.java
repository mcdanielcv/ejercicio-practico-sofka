package com.microservicio.cliente.persona.cliente_persona.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private Long clientId;
    private String name;
    private String address;
}
