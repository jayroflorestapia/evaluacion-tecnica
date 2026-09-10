package mx.evaluacion.transacciones.service;

import jakarta.transaction.Transactional;
import mx.evaluacion.transacciones.dto.CancelarRequest;
import mx.evaluacion.transacciones.dto.OperacionRequest;
import mx.evaluacion.transacciones.dto.OperacionResponse;
import mx.evaluacion.transacciones.entity.Transaccion;
import mx.evaluacion.transacciones.repository.TransaccionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class TransaccionService {
    private final TransaccionRepository repository;
    private final SecureRandom random = new SecureRandom();
    public TransaccionService(TransaccionRepository repository) { this.repository = repository; }

    @Transactional
    public OperacionResponse crear(OperacionRequest request) {
        Transaccion t = new Transaccion();
        t.setOperacion(request.operacion()); t.setImporte(request.importe());
        t.setCliente(request.cliente()); t.setSecreto(request.secreto()); t.setEstatus("Aprobada");
        repository.save(t);
        String referencia;
        do { referencia = String.format("%06d", random.nextInt(1_000_000)); }
        while (repository.findByReferencia(referencia).isPresent());
        t.setReferencia(referencia);
        return new OperacionResponse(t.getId(), t.getEstatus(), t.getReferencia(), t.getOperacion());
    }

    @Transactional
    public void cancelar(CancelarRequest request) {
        if (!"cancelar".equalsIgnoreCase(request.estatus()) || repository.cancelar(request.id(), request.referencia()) == 0) {
            throw new IllegalArgumentException("No existe una transaccion aprobada con ese id y referencia");
        }
    }
    public Page<Transaccion> listar(int page, int size, String sortBy, Sort.Direction direction) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }
}
