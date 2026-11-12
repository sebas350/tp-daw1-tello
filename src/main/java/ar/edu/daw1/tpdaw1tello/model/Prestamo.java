
package ar.edu.daw1.tpdaw1tello.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "debe indicar un libro")
    @Column(nullable = false)
    private Long libroId;

    @NotNull(message = "debe indicar un socio")
    @Column(nullable = false)
    private Long socioId;

    @NotNull(message = "debe indicar una fecha de devolución")
    @Future(message = "debe ser una fecha futura")
    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    @Column
    private LocalDate fechaRealDevolucion;

    @Column
    private java.math.BigDecimal multa = java.math.BigDecimal.ZERO;

    @Column
    private Boolean multaPagada = false;

    @Column
    private Boolean renovado = false;

    @Column(nullable = false)
    private String estado = "ACTIVO";

    public Prestamo() {
    }

    public Prestamo(Long libroId, Long socioId, LocalDate fechaDevolucion) {
        this.libroId = libroId;
        this.socioId = socioId;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = "ACTIVO";
    }

    public Long getId() {
        return id;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRealDevolucion() {
        return fechaRealDevolucion;
    }

    public void setFechaRealDevolucion(LocalDate fechaRealDevolucion) {
        this.fechaRealDevolucion = fechaRealDevolucion;
    }

    public java.math.BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(java.math.BigDecimal multa) {
        this.multa = multa;
    }

    public boolean isMultaPagada() {
        return Boolean.TRUE.equals(multaPagada);
    }

    public void setMultaPagada(Boolean multaPagada) {
        this.multaPagada = multaPagada;
    }

    public boolean isRenovado() {
        return Boolean.TRUE.equals(renovado);
    }

    public void setRenovado(Boolean renovado) {
        this.renovado = renovado;
    }
}
