package mx.evaluacion.entrada.dto;
import java.math.BigDecimal;
public record OperacionInternaRequest(String operacion, BigDecimal importe, String cliente, String secreto) { }
