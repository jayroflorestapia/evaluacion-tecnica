package mx.evaluacion.transacciones.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String operacion;
    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal importe;
    @Column(nullable = false) private String cliente;
    @Column(unique = true) private String referencia;
    @Column(nullable = false) private String estatus;
    @Column(nullable = false) private String secreto;
    public Long getId() { return id; }
    public String getOperacion() { return operacion; }
    public BigDecimal getImporte() { return importe; }
    public String getCliente() { return cliente; }
    public String getReferencia() { return referencia; }
    public String getEstatus() { return estatus; }
    public String getSecreto() { return secreto; }
    public void setOperacion(String value) { operacion = value; }
    public void setImporte(BigDecimal value) { importe = value; }
    public void setCliente(String value) { cliente = value; }
    public void setReferencia(String value) { referencia = value; }
    public void setEstatus(String value) { estatus = value; }
    public void setSecreto(String value) { secreto = value; }
}
