package mx.evaluacion.transacciones.dto;

import mx.evaluacion.transacciones.entity.Transaccion;
import java.math.BigDecimal;

/** Vista segura para el listado: el secreto nunca se expone fuera del servicio. */
public record TransaccionListadoResponse(Long id, String operacion, BigDecimal importe, String cliente,
                                         String referencia, String estatus) {
    public static TransaccionListadoResponse from(Transaccion transaction) {
        return new TransaccionListadoResponse(transaction.getId(), transaction.getOperacion(), transaction.getImporte(),
                transaction.getCliente(), transaction.getReferencia(), transaction.getEstatus());
    }
}
