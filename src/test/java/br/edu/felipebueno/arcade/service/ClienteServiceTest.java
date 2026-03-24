package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.repository.memory.InMemoryClienteRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteServiceTest {

    @Test
    void deveCadastrarClienteComSucesso() {
        ClienteService clienteService = new ClienteService(new InMemoryClienteRepository());

        Cliente cliente = clienteService.cadastrar(
                "Felipe Bueno",
                "000.000.000-00",
                "(43) 99999-0000",
                "felipe@example.com"
        );

        assertEquals(1L, cliente.getId());
        assertEquals("Felipe Bueno", cliente.getNome());
    }

    @Test
    void deveImpedirCpfDuplicado() {
        ClienteService clienteService = new ClienteService(new InMemoryClienteRepository());

        clienteService.cadastrar(
                "Felipe Bueno",
                "000.000.000-00",
                "(43) 99999-0000",
                "felipe@example.com"
        );

        assertThrows(BusinessException.class, () -> clienteService.cadastrar(
                "Outro Cliente",
                "000.000.000-00",
                "(43) 98888-0000",
                "outro@example.com"
        ));
    }
}
