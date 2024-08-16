package com.microservicio.cuenta.movimiento.cuenta_movimiento.com.microservicio.account.transaction.account_transaction.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MOVEMENT_CLIENT_QUEUE = "movementClientQueue";

    @Bean
    public Queue movimientoAClienteQueue() {
        return new Queue(MOVEMENT_CLIENT_QUEUE, false);
    }
}
