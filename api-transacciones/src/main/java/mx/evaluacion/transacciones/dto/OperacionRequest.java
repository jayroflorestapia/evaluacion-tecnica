package mx.evaluacion.transacciones.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OperacionRequest(
    @NotBlank @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúÑñ ]+", message = "operacion solo admite letras") String operacion,
    @NotNull @DecimalMin(value = "0.01", message = "importe debe ser mayor a cero") @Digits(integer = 13, fraction = 2, message = "importe debe tener maximo 2 decimales") BigDecimal importe,
    @NotBlank @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúÑñ ]+", message = "cliente solo admite letras") String cliente,
    @NotBlank String secreto) { }
