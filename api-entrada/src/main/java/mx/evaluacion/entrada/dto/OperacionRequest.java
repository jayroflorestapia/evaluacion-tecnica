package mx.evaluacion.entrada.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OperacionRequest(
    @NotBlank @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúÑñ ]+", message = "operacion solo admite letras") String operacion,
    @NotNull @DecimalMin(value = "0.01") @Digits(integer = 13, fraction = 2, message = "importe debe tener maximo 2 decimales") BigDecimal importe,
    @NotBlank @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúÑñ ]+", message = "cliente solo admite letras") String cliente,
    @NotBlank(message = "secreto cifrado es obligatorio") String secreto) { }
