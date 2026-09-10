package mx.evaluacion.entrada.controller;

import jakarta.validation.Valid;
import mx.evaluacion.entrada.dto.OperacionRequest;
import mx.evaluacion.entrada.dto.OperacionResponse;
import mx.evaluacion.entrada.service.OperacionService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operaciones")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class OperacionController {
    private final OperacionService service;
    public OperacionController(OperacionService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<OperacionResponse> crear(@Valid @RequestBody OperacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
}
