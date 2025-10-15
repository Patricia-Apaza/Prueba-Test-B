package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pe.edu.upeu.sysalmacen.modelo.Producto;
import pe.edu.upeu.sysalmacen.modelo.Marca;
import pe.edu.upeu.sysalmacen.modelo.Categoria;
import pe.edu.upeu.sysalmacen.modelo.UnidadMedida;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Rollback(false)
public class IProductoRepositoryTest {

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IUnidadMedidaRepository unidadMedidaRepository;

    private static Long productoId;

    private Categoria categoria;
    private Marca marca;
    private UnidadMedida unidadMedida;

    @BeforeEach
    public void setUp() {
        categoria = new Categoria();
        categoria.setNombre("Electronics");
        categoriaRepository.save(categoria);

        marca = new Marca();
        marca.setNombre("Samsung");
        marcaRepository.save(marca);

        unidadMedida = new UnidadMedida();
        unidadMedida.setNombreMedida("Unidad");
        unidadMedidaRepository.save(unidadMedida);

        Producto producto = new Producto();
        producto.setNombre("Samsung Galaxy S21");
        producto.setPu(699.99);
        producto.setPuOld(650.00);
        producto.setUtilidad(10.0);
        producto.setStock(50.0);
        producto.setStockOld(45.0);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setUnidadMedida(unidadMedida);

        Producto guardado = productoRepository.save(producto);
        productoId = guardado.getIdProducto();
    }

    @Test
    @Order(1)
    public void testGuardarProducto() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("LG Velvet");
        nuevoProducto.setPu(499.99);
        nuevoProducto.setPuOld(450.00);
        nuevoProducto.setUtilidad(15.0);
        nuevoProducto.setStock(100.0);
        nuevoProducto.setStockOld(95.0);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setMarca(marca);
        nuevoProducto.setUnidadMedida(unidadMedida);

        Producto guardado = productoRepository.save(nuevoProducto);

        assertNotNull(guardado.getIdProducto());
        assertEquals("LG Velvet", guardado.getNombre());
    }

    @Test
    @Order(2)
    public void testBuscarProductoPorId() {
        Optional<Producto> producto = productoRepository.findById(productoId);
        assertTrue(producto.isPresent());
        assertEquals("Samsung Galaxy S21", producto.get().getNombre());
    }

    @Test
    @Order(3)
    public void testActualizarProducto() {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        producto.setNombre("Samsung Galaxy S22");
        Producto actualizado = productoRepository.save(producto);

        assertEquals("Samsung Galaxy S22", actualizado.getNombre());
    }

    @Test
    @Order(4)
    public void testListarProductos() {
        List<Producto> productos = productoRepository.findAll();
        assertFalse(productos.isEmpty());
        System.out.println("Total productos registrados: " + productos.size());
        for (Producto p : productos) {
            System.out.println(p.getNombre() + "\t" + p.getIdProducto());
        }
    }

    @Test
    @Order(5)
    public void testEliminarProducto() {
        productoRepository.deleteById(productoId);
        Optional<Producto> eliminado = productoRepository.findById(productoId);
        assertFalse(eliminado.isPresent(), "El producto debería haber sido eliminado");
    }
}
