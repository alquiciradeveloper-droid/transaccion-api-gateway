package aalquicira.api.gateway.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aalquicira.api.gateway.Client.ApiClient;
import aalquicira.api.gateway.Dto.TransaccionRequest;
import aalquicira.api.gateway.Dto.TransaccionResponse;
import aalquicira.api.gateway.Util.CryptoUtil;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class TransaccionControllerTest {

	private static final String SECRET_KEY = "12345678901234567890123456789012";
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApiClient apiClient;

    private TransaccionRequest transaccionRequest;

    @BeforeEach
    void setUp() throws Exception{
        transaccionRequest = new TransaccionRequest();
        transaccionRequest.setOperacion("VENTA");
        transaccionRequest.setImporte("150.50");
        transaccionRequest.setCliente("ClienteTest");
        transaccionRequest.setSecreto(CryptoUtil.encrypt("SecretoTEST", SECRET_KEY));
    }

    @Test
    @DisplayName("Debe retornar 200 OK cuando los datos de la transacción son válidos")
    void registrar_DebeRetornar200_CuandoRequestEsValido() throws Exception {
    	
        TransaccionResponse responseMock = new TransaccionResponse(
                "1", 
                "EXITOSO", 
                "REF-9999", 
                "PAGO"
        );

        when(apiClient.guardarTransaccion(any(TransaccionRequest.class)))
                .thenReturn(responseMock);

        mockMvc.perform(post("/api/transacciones")
        				.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 400 Bad Request cuando el importe tiene más de 2 decimales")
    void registrar_DebeRetornar400_CuandoImporteTieneMasDeDosDecimales() throws Exception {
    	TransaccionRequest requestInvalido = new TransaccionRequest();
        requestInvalido.setImporte("150.555"); // Formato inválido
        requestInvalido.setCliente("CLI12345");

        mockMvc.perform(post("/api/transacciones")
        				.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.importe").exists());
    }

    @Test
    @DisplayName("Debe retornar 400 Bad Request cuando los campos obligatorios están vacíos")
    void registrar_DebeRetornar400_CuandoCamposEstanVacios() throws Exception {
    	TransaccionRequest requestVacio = new TransaccionRequest();
        requestVacio.setImporte("");
        requestVacio.setCliente("");

        mockMvc.perform(post("/api/transacciones")
        				.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestVacio)))
                .andExpect(status().isBadRequest());
    }
}
