package aalquicira.api.gateway.Dto;

public class TransaccionResponse {

    private String id;
    private String estatus;
    private String referencia;
    private String operacion;

    public TransaccionResponse() {
    }

    public TransaccionResponse(String id, String estatus, String referencia, String operacion) {
        this.id = id;
        this.estatus = estatus;
        this.referencia = referencia;
        this.operacion = operacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}
