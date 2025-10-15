package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pe.edu.upeu.sysalmacen.modelo.Cliente;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Rollback(false)
public class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository clienteRepository;

    private static String clienteDniRuc;

    @BeforeEach
    public void setUp() {
        Cliente cliente = new Cliente();
        cliente.setDniruc("12345678901");
        cliente.setNombres("Juan Perez");
        cliente.setRepLegal("Pedro Perez");
        cliente.setTipoDocumento("DNI");
        cliente.setDireccion("Av. Siempre Viva 123");

        Cliente guardado = clienteRepository.save(cliente);
        clienteDniRuc = guardado.getDniruc();
    }

    @Test
    @Order(1)
    public void testGuardarCliente() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDniruc("98765432101");
        nuevoCliente.setNombres("Maria Lopez");
        nuevoCliente.setRepLegal("Ana Lopez");
        nuevoCliente.setTipoDocumento("DNI");
        nuevoCliente.setDireccion("Calle Falsa 456");

        Cliente guardado = clienteRepository.save(nuevoCliente);

        assertNotNull(guardado.getDniruc());
        assertEquals("Maria Lopez", guardado.getNombres());
    }

    @Test
    @Order(2)
    public void testBuscarClientePorId() {
        Optional<Cliente> cliente = clienteRepository.findById(clienteDniRuc);
        assertTrue(cliente.isPresent());
        assertEquals("Juan Perez", cliente.get().getNombres());
    }

    @Test
    @Order(3)
    public void testActualizarCliente() {
        Cliente cliente = clienteRepository.findById(clienteDniRuc).orElseThrow();
        cliente.setNombres("Juan Perez Actualizado");
        Cliente actualizado = clienteRepository.save(cliente);

        assertEquals("Juan Perez Actualizado", actualizado.getNombres());
    }

    @Test
    @Order(4)
    public void testListarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        assertFalse(clientes.isEmpty());
        System.out.println("Total clientes registrados: " + clientes.size());
        for (Cliente c : clientes) {
            System.out.println(c.getNombres() + "\t" + c.getDniruc());
        }
    }

    @Test
    @Order(5)
    public void testEliminarCliente() {
        clienteRepository.deleteById(clienteDniRuc);
        Optional<Cliente> eliminado = clienteRepository.findById(clienteDniRuc);
        assertFalse(eliminado.isPresent(), "El cliente debería haber sido eliminado");
    }
}
