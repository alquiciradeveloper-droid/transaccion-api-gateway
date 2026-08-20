package aalquicira.api.transaccion.Controller;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Entity.Transaccion;
import aalquicira.api.transaccion.Service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api2/transacciones")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponse> procesarTransaccion(@Valid @RequestBody TransaccionRequest request) {
        TransaccionResponse response = transaccionService.procesarTransaccion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
    
    @GetMapping
    public ResponseEntity<Page<Transaccion>> listarTransacciones(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
        // Spring Boot mapea automáticamente page, size y sort de la URL directamente a este Pageable
        Page<Transaccion> pagina = transaccionService.obtenerTodas(pageable);
        return ResponseEntity.ok(pagina);
    }
    
    @PatchMapping
    public ResponseEntity<Map<String, String>> cancelarTransaccion(@RequestBody TransaccionResponse dto) {
        try {
            boolean cancelado = transaccionService.cancelarTransaccion(dto);
            if (cancelado) {
                return ResponseEntity.ok(Map.of(
                    "id", dto.getId(),
                    "estatus", "Cancelada",
                    "referencia", dto.getReferencia(),
                    "mensaje", "Transacción cancelada exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la transacción con el ID y referencia indicados"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}