package mx.evaluacion.transacciones.dto;

import jakarta.validation.constraints.*;

public record CancelarRequest(@NotNull Long id, @NotBlank @Pattern(regexp = "\\d{6}") String referencia,
                              @NotBlank String estatus) { }
