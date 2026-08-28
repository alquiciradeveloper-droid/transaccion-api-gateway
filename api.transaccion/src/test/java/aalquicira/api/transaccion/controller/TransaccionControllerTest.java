package aalquicira.api.transaccion.Controller;

import aalquicira.api.transaccion.Dto.TransaccionRequest;
import aalquicira.api.transaccion.Dto.TransaccionResponse;
import aalquicira.api.transaccion.Service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransaccionService transaccionService;

    @Test
    @DisplayName("Debe retornar 200 OK al recibir un request válido en el microservicio interno")
    void registrar_DebeRetornar200() throws Exception {
        TransaccionRequest request = new TransaccionRequest();
        request.setOperacion("VENTA");        
        request.setImporte("100.00");
        request.setCliente("ClienteTEST");
        request.setSecreto("Secreto");

        TransaccionResponse responseMock = new TransaccionResponse("1", "EXITOSO", "REF-001", "PAGO");

        when(transaccionService.procesarTransaccion(any(TransaccionRequest.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api2/transacciones")
        				.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
