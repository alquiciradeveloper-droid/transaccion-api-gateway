package aalquicira.api.transaccion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Service.TransaccionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TransaccionService service;

    @Test
    @DisplayName("Debe responder HTTP 200 OK con el DTO esperado cuando el JSON de entrada es válido")
    void procesar_RespuestaOk() throws Exception {
        TransaccionRequest request = new TransaccionRequest("venta", "100.00", "Angel", "secretoCifrado123");
        TransaccionResponse mockResponse = new TransaccionResponse("2376", "Aprobada", "262737", "venta");

        when(service.procesarTransaccion(any(TransaccionRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2376"))
                .andExpect(jsonPath("$.estatus").value("Aprobada"))
                .andExpect(jsonPath("$.referencia").value("262737"))
                .andExpect(jsonPath("$.operacion").value("venta"));
    }

    @Test
    @DisplayName("Debe responder HTTP 400 Bad Request cuando las validaciones Jakarta fallan (ej. campos vacíos o números en nombre)")
    void procesar_BadRequest_ValidacionFallida() throws Exception {
        // Arrange: Importe y cliente no cumplen con las reglas @Pattern
        TransaccionRequest requestInvalido = new TransaccionRequest("venta123","invalido", "", "");

        // Act & Assert
        mockMvc.perform(post("/api/v1/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.operacion").exists())
                .andExpect(jsonPath("$.importe").exists())
                .andExpect(jsonPath("$.cliente").exists())
                .andExpect(jsonPath("$.secreto").exists());
    }
}


