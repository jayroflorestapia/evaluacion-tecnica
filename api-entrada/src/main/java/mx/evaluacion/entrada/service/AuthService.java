package mx.evaluacion.entrada.service;

import jakarta.annotation.PostConstruct;
import mx.evaluacion.entrada.dto.LoginRequest;
import mx.evaluacion.entrada.entity.Usuario;
import mx.evaluacion.entrada.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository users) { this.users = users; }

    @PostConstruct
    void seedUser() {
        if (users.findByUsername("angel").isEmpty()) {
            Usuario user = new Usuario();
            user.setUsername("angel");
            user.setPasswordHash(encoder.encode("Password123!"));
            users.save(user);
        }
    }

    public boolean validarCredenciales(LoginRequest request) {
        return users.findByUsername(request.usuario())
                .map(user -> encoder.matches(request.password(), user.getPasswordHash()))
                .orElse(false);
    }
}
