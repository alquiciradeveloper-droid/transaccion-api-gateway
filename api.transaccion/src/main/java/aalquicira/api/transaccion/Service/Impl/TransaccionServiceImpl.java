package aalquicira.api.transaccion.Service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Entity.Transaccion;
import aalquicira.api.transaccion.Repository.TransaccionRepository;
import aalquicira.api.transaccion.Service.TransaccionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    @Transactional
    public TransaccionResponse procesarTransaccion(TransaccionRequest request) {

        // Conversión a BigDecimal garantizando 2 posiciones decimales de moneda
        BigDecimal importeBigDecimal = new BigDecimal(request.getImporte())
                .setScale(2, RoundingMode.HALF_UP);

        Transaccion entity = new Transaccion();
        entity.setOperacion(request.getOperacion());    
        entity.setImporte(importeBigDecimal);
        entity.setCliente(request.getCliente());
        entity.setReferencia(generarReferencia());
        entity.setEstatus("Aprobada");
        // Asignación directa: la API 1 ya lo descifró previamente
        entity.setSecreto(request.getSecreto());

        Transaccion savedEntity = transaccionRepository.save(entity);
        return new TransaccionResponse(
            String.valueOf(savedEntity.getId()),
            savedEntity.getEstatus(),
            savedEntity.getReferencia(),
            savedEntity.getOperacion()  
        );
    }

    private String generarReferencia() {
        int numeroAleatorio = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(numeroAleatorio);
    }
    
    @Override
    public Page<Transaccion> obtenerTodas(Pageable pageable) {
        return transaccionRepository.findAll(pageable);
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }
    
    public boolean cancelarTransaccion(TransaccionResponse dto) {
        // Validar que el estatus enviado sea "cancelar"
        if (!"cancelar".equalsIgnoreCase(dto.getEstatus())) {
            throw new IllegalArgumentException("El estatus enviado debe ser 'cancelar'");
        }

        // Convertir String a Long para empatar con la PK de la entidad
        Long idLong = Long.parseLong(dto.getId());

        int filasAfectadas = transaccionRepository.cancelarTransaccion(idLong, dto.getReferencia());
        return filasAfectadas > 0;
    }
}