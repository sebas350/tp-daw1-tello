package ar.edu.daw1.tpdaw1tello.dto;

import ar.edu.daw1.tpdaw1tello.model.Libro;

public record LibroRankingDTO(
        Libro libro,
        long cantidadPrestamos
) {
}