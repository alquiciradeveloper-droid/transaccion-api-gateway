package aalquicira.api.gateway.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import aalquicira.api.gateway.Client.ApiClient;
import aalquicira.api.gateway.Dto.TransaccionRequest;
import aalquicira.api.gateway.Dto.TransaccionResponse;
import aalquicira.api.gateway.Util.CryptoUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class TransaccionController {

    private final ApiClient apiClient;
    
    @Value("${app.security.aes-key:12345678901234567890123456789012}")
    private String secretKey;

    public TransaccionController(ApiClient api2Client) {
        this.apiClient = api2Client;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponse> registrar(@Valid @RequestBody TransaccionRequest dto) {
    	String secretoDescifrado;

        try {
            secretoDescifrado = CryptoUtil.decrypt(dto.getSecreto(), secretKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("No se puede descifrar el atributo secreto. Verifique el payload: " + e.getMessage());
        }

        dto.setSecreto(secretoDescifrado);
        TransaccionResponse respuesta = apiClient.guardarTransaccion(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id,desc") String sort) {
        return ResponseEntity.ok(apiClient.listarTransacciones(page, size, sort));
    }

    @PatchMapping
    public ResponseEntity<Object> cancelar(@RequestBody Object dto) {
        return ResponseEntity.ok(apiClient.cancelarTransaccion(dto));
    }
}
