package mx.evaluacion.entrada.controller;

import jakarta.validation.Valid;
import mx.evaluacion.entrada.dto.LoginRequest;
import mx.evaluacion.entrada.service.AuthService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { 
        this.service = service; 
    }
    @PostMapping("/login") public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        if (!service.validarCredenciales(request)) 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario o password incorrectos"));
        return ResponseEntity.ok(Map.of("message", "Login correcto"));
    }
}
