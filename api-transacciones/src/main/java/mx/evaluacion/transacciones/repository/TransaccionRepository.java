package mx.evaluacion.transacciones.repository;

import mx.evaluacion.transacciones.entity.Transaccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    Optional<Transaccion> findByReferencia(String referencia);

    @Modifying
    @Query("update Transaccion t set t.estatus = 'Cancelada' where t.id = :id and t.referencia = :referencia and t.estatus = 'Aprobada'")
    int cancelar(@Param("id") Long id, @Param("referencia") String referencia);
}
