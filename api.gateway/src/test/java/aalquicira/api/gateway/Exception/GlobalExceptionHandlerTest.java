package aalquicira.api.gateway.Exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Debe capturar MethodArgumentNotValidException y retornar un Map con los errores y HTTP 400")
    void handleValidationExceptions_DebeRetornarMapaDeErrores() throws NoSuchMethodException {
        // Arrange: Construcción del error de validación mockeado
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "transaccionDTO");
        bindingResult.addError(new FieldError("transaccionDTO", "importe", "El importe debe ser numérico con máximo 2 decimales"));

        MethodParameter parameter = new MethodParameter(
                this.getClass().getDeclaredMethod("setUp"), -1
        );
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("importe"));
        assertEquals("El importe debe ser numérico con máximo 2 decimales", response.getBody().get("importe"));
    }
}
