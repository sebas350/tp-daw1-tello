
package ar.edu.daw1.tpdaw1tello.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "no debe estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "no debe estar vacío")

    @Email(message = "debe tener un formato de email válido")
    @Column(nullable = false)
    private String email;

    @Column
    private Boolean suspendido = false;

    public Socio() {
    }

    public Socio(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuspendido() {
        return Boolean.TRUE.equals(suspendido);
    }

    public void setSuspendido(Boolean suspendido) {
        this.suspendido = suspendido;
    }
}
