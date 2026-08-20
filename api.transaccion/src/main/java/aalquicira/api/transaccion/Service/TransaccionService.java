package aalquicira.api.transaccion.Service;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Entity.Transaccion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransaccionService {

    TransaccionResponse procesarTransaccion(TransaccionRequest request);

    Page<Transaccion> obtenerTodas(Pageable pageable);

    Transaccion guardar(Transaccion transaccion);
    
    boolean cancelarTransaccion(TransaccionResponse dto);
}
