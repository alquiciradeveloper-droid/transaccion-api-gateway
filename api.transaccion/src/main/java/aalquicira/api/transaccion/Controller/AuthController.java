package aalquicira.api.transaccion.Controller;

import aalquicira.api.transaccion.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api2/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> validarCredenciales(@RequestBody Map<String, String> body) {
        String username = body.get("usuario");
        String rawPassword = body.get("password");

        boolean esValido = usuarioRepository.findByUsuario(username)
                .map(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .orElse(false);

        return ResponseEntity.ok(esValido);
    }
}
