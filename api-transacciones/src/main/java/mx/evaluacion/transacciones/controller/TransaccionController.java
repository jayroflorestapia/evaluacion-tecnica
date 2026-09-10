package mx.evaluacion.transacciones.controller;

import jakarta.validation.Valid;
import mx.evaluacion.transacciones.dto.CancelarRequest;
import mx.evaluacion.transacciones.dto.OperacionRequest;
import mx.evaluacion.transacciones.dto.OperacionResponse;
import mx.evaluacion.transacciones.dto.TransaccionListadoResponse;
import mx.evaluacion.transacciones.entity.Transaccion;
import mx.evaluacion.transacciones.service.TransaccionService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class TransaccionController {
    private final TransaccionService service;
    public TransaccionController(TransaccionService service) { this.service = service; }
    @PostMapping public ResponseEntity<OperacionResponse> crear(@Valid @RequestBody OperacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
    @PatchMapping("/cancelar") public ResponseEntity<Void> cancelar(@Valid @RequestBody CancelarRequest request) {
        service.cancelar(request); return ResponseEntity.noContent().build();
    }
    @GetMapping public Page<TransaccionListadoResponse> listar(@RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
       @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return service.listar(page, size, sortBy, direction).map(TransaccionListadoResponse::from);
    }
}
