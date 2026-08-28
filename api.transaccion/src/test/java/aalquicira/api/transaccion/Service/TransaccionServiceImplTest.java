package aalquicira.api.transaccion.Service;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Entity.Transaccion;
import aalquicira.api.transaccion.Repository.TransaccionRepository;
import aalquicira.api.transaccion.Service.Impl.TransaccionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private TransaccionRequest request;
    private Transaccion entityGuardada;

    @BeforeEach
    void setUp() {
        request = new TransaccionRequest();
        request.setOperacion("VENTA");        
        request.setImporte("100.00");
        request.setCliente("ClienteTEST001");
        request.setSecreto("Secreto");

        entityGuardada = new Transaccion();
        entityGuardada.setOperacion("VENTA");
        entityGuardada.setImporte(new BigDecimal("500.50"));
        entityGuardada.setCliente("ClienteTEST");
        entityGuardada.setReferencia("12345REF");
        entityGuardada.setEstatus("Aprobada");
        entityGuardada.setSecreto("SecretoTest00123");
    }

    @Test
    @DisplayName("Debe procesar y guardar la transacción devolviendo una respuesta exitosa")
    void registrarTransaccion_Exito() {
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(entityGuardada);
        TransaccionResponse respuesta = transaccionService.procesarTransaccion(request);

        // Assert
        assertNotNull(respuesta);
        assertEquals("Aprobada", respuesta.getEstatus());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }
}
