package com.microservicio.cliente.persona.cliente_persona.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Person {

    @NotBlank
    private String name;
    private String gender;
    private int age;
    private String identification;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
}
