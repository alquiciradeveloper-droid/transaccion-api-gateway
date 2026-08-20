package aalquicira.api.transaccion.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String operacion;

    @Column(precision = 12, scale = 2)
    private BigDecimal importe;

    @Column(length = 100)
    private String cliente;

    @Column(length = 50)
    private String referencia;

    @Column(length = 20)       
    private String estatus;

    @Column(length = 1000)
    private String secreto;
}