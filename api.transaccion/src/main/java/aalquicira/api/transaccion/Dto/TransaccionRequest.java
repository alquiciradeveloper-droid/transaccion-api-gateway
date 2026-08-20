package aalquicira.api.transaccion.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TransaccionRequest {

	@NotBlank(message = "La operación es obligatoria")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El atributo operación debe contener únicamente caracteres (letras)")
    @Size(min = 2, max = 20, message = "La operación debe tener entre 2 y 20 caracteres")
    private String operacion;

    @NotNull(message = "El importe es obligatorio")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Formato de importe inválido. Ejemplo: 10.10 o 100")
    private String importe;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El atributo cliente debe contener únicamente caracteres (letras)")
    @Size(min = 2, max = 50, message = "El nombre del cliente debe tener entre 2 y 50 caracteres")
    private String cliente;

    @NotNull(message = "El secreto cifrado es obligatorio")
    private String secreto;

    public TransaccionRequest() {
    }

    public TransaccionRequest(String operacion, String importe, String cliente, String secreto) {
        this.operacion = operacion;
        this.importe = importe;
        this.cliente = cliente;
        this.secreto = secreto;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }
}