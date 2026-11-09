
package ar.edu.daw1.tpdaw1tello.repository;

import ar.edu.daw1.tpdaw1tello.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
