
package ar.edu.daw1.tpdaw1tello.repository;

import ar.edu.daw1.tpdaw1tello.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findBySocioId(Long socioId);

    List<Prestamo> findByEstado(String estado);

    List<Prestamo> findByLibroId(Long libroId);

    boolean existsByLibroIdAndEstadoNot(Long libroId, String estado);

    long countBySocioIdAndEstadoNot(Long socioId, String estado);

}
