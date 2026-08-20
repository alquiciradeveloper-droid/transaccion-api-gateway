package aalquicira.api.gateway.Controller;

import aalquicira.api.gateway.Client.ApiClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final ApiClient apiClient;

    public AuthController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("usuario");
        String rawPassword = body.get("password");

        // Se delega la validación de BCrypt y la consulta BD a la API 2 mediante Feign
        boolean esValido = apiClient.validarCredenciales(body);

        if (esValido) {
            System.out.println(" Login exitoso para el usuario: " + username);
            return ResponseEntity.ok(Map.of("mensaje", "Login Exitoso", "usuario", username));
        } else {
            System.out.println(" Credenciales inválidas devueltas por API 2.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales invalidas"));
        }
    }
}