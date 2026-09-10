package mx.evaluacion.entrada.entity;
import jakarta.persistence.*;

@Entity @Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique = true, nullable = false) private String username;
    @Column(nullable = false) private String passwordHash;
    public String getUsername() { return username; } public String getPasswordHash() { return passwordHash; }
    public void setUsername(String value) { username = value; } public void setPasswordHash(String value) { passwordHash = value; }
}
