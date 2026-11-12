
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Prestamo;
import ar.edu.daw1.tpdaw1tello.repository.PrestamoRepository;
import ar.edu.daw1.tpdaw1tello.repository.LibroRepository;
import ar.edu.daw1.tpdaw1tello.repository.SocioRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import ar.edu.daw1.tpdaw1tello.model.Socio;

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

        Socio socio = socioRepository.findById(prestamo.getSocioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Socio no encontrado"
                ));

        if (socio.isSuspendido()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El socio está suspendido por tener multas pendientes"
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

    public Prestamo cambiarEstado(Long id, String nuevoEstado) {

        Prestamo prestamo = buscarPorId(id);

        if (!nuevoEstado.equals("ACTIVO")
                && !nuevoEstado.equals("VENCIDO")
                && !nuevoEstado.equals("DEVUELTO")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado no válido"
            );
        }

        boolean transicionValida =
                (prestamo.getEstado().equals("ACTIVO")
                        && (nuevoEstado.equals("VENCIDO")
                        || nuevoEstado.equals("DEVUELTO")))
                        ||
                        (prestamo.getEstado().equals("VENCIDO")
                                && nuevoEstado.equals("DEVUELTO"));

        if (!transicionValida) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No está permitido realizar esta transición de estado"
            );
        }

        if (nuevoEstado.equals("DEVUELTO")) {

            LocalDate fechaReal = LocalDate.now();
            prestamo.setFechaRealDevolucion(fechaReal);
            prestamo.setMulta(BigDecimal.ZERO);

            if (fechaReal.isAfter(prestamo.getFechaDevolucion())) {

                long diasDemora = ChronoUnit.DAYS.between(
                        prestamo.getFechaDevolucion(),
                        fechaReal
                );

                BigDecimal multa = BigDecimal.valueOf(diasDemora)
                        .multiply(BigDecimal.valueOf(500));

                prestamo.setMulta(multa);
            }
        }

        prestamo.setEstado(nuevoEstado);

        return prestamoRepository.save(prestamo);
    }

    public Prestamo renovar(Long id) {

        Prestamo prestamo = buscarPorId(id);

        if (!prestamo.getEstado().equals("ACTIVO")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden renovar préstamos activos"
            );
        }

        if (prestamo.isRenovado()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El préstamo ya fue renovado"
            );
        }

        prestamo.setFechaDevolucion(
                prestamo.getFechaDevolucion().plusDays(7)
        );

        prestamo.setRenovado(true);

        return prestamoRepository.save(prestamo);
    }

    public Prestamo pagarMulta(Long id) {

        Prestamo prestamo = buscarPorId(id);

        if (prestamo.getMulta() == null ||
                prestamo.getMulta().compareTo(BigDecimal.ZERO) <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El préstamo no tiene una multa pendiente"
            );
        }

        if (prestamo.isMultaPagada()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La multa ya fue pagada"
            );
        }

        prestamo.setMultaPagada(true);
        prestamoRepository.save(prestamo);

        boolean tieneMultasPendientes = prestamoRepository
                .findBySocioId(prestamo.getSocioId())
                .stream()
                .anyMatch(p ->
                        p.getMulta() != null
                                && p.getMulta().compareTo(BigDecimal.ZERO) > 0
                                && !p.isMultaPagada()
                );

        if (!tieneMultasPendientes) {
            Socio socio = socioRepository.findById(prestamo.getSocioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Socio no encontrado"
                    ));

            socio.setSuspendido(false);
            socioRepository.save(socio);
        }

        return prestamo;
    }
}

