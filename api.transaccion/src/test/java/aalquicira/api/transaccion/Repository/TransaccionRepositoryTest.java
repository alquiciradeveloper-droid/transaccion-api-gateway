package aalquicira.api.transaccion.Repository;

import aalquicira.api.transaccion.Entity.Transaccion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransaccionRepositoryTest {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Test
    @DisplayName("Debe guardar y recuperar una entidad Transaccion en la base de datos")
    void guardarYBuscarTransaccion_Exito() {
        Transaccion transaccion = new Transaccion();
        transaccion.setOperacion("VENTA");
        transaccion.setImporte(new BigDecimal("200.00"));
        transaccion.setCliente("ClienteTEST");
        transaccion.setReferencia("123REF");
        transaccion.setEstatus("Aprobada");
        transaccion.setSecreto("SecretoTest001");

        Transaccion guardada = transaccionRepository.save(transaccion);
        Optional<Transaccion> encontrada = transaccionRepository.findById(guardada.getId());

        // Assert
        assertTrue(encontrada.isPresent());
        assertEquals("123REF", encontrada.get().getReferencia());
        assertEquals("ClienteTEST", encontrada.get().getCliente());
    }
}
