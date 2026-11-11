
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Prestamo;
import ar.edu.daw1.tpdaw1tello.repository.PrestamoRepository;
import ar.edu.daw1.tpdaw1tello.repository.LibroRepository;
import ar.edu.daw1.tpdaw1tello.repository.SocioRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository) {

        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    public Prestamo crear(Prestamo prestamo) {

        if (!libroRepository.existsById(prestamo.getLibroId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Libro no encontrado"
            );
        }

        if (!socioRepository.existsById(prestamo.getSocioId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Socio no encontrado"
            );
        }

        if (prestamoRepository.existsByLibroIdAndEstadoNot(
                prestamo.getLibroId(), "DEVUELTO")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El libro no está disponible para préstamo"
            );
        }

        long prestamosPendientes =
                prestamoRepository.countBySocioIdAndEstadoNot(
                        prestamo.getSocioId(), "DEVUELTO");

        if (prestamosPendientes >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El socio ya tiene el máximo de tres préstamos pendientes"
            );
        }

        prestamo.setEstado("ACTIVO");

        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listar() {
        return prestamoRepository.findAll();
    }

    public List<Prestamo> listarPorEstado(String estado) {
        return prestamoRepository.findByEstado(estado);
    }

    public List<Prestamo> listarPorSocio(Long socioId) {

        if (!socioRepository.existsById(socioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Socio no encontrado"
            );
        }

        return prestamoRepository.findBySocioId(socioId);
    }

    public Prestamo buscarPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Préstamo no encontrado"
                ));
    }
}

