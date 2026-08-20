package aalquicira.api.gateway.Client;

import aalquicira.api.gateway.Dto.TransaccionRequest;
import aalquicira.api.gateway.Dto.TransaccionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "api2-service", url = "http://localhost:8081/api2")
public interface ApiClient {

    // Cambia LoginRequestDto por Map<String, String>
    @PostMapping("/auth/login")
    boolean validarCredenciales(@RequestBody Map<String, String> body);

    // --- MÓDULO DE TRANSACCIONES ---
    @PostMapping("/transacciones")
    TransaccionResponse guardarTransaccion(@RequestBody TransaccionRequest dto);

    @GetMapping("/transacciones")
    Object listarTransacciones(@RequestParam("page") int page, 
                                @RequestParam("size") int size, 
                                @RequestParam("sort") String sort);

    @PatchMapping("/transacciones")
    Object cancelarTransaccion(@RequestBody Object dto);
}