
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Socio;
import ar.edu.daw1.tpdaw1tello.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ar.edu.daw1.tpdaw1tello.repository.PrestamoRepository;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;

    public SocioService(
            SocioRepository socioRepository,
            PrestamoRepository prestamoRepository
    ) {
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Socio crear(Socio socio) {
        return socioRepository.save(socio);
    }

    public List<Socio> listar() {
        return socioRepository.findAll();
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Socio no encontrado"
                ));
    }

    public Socio actualizar(Long id, Socio socioActualizado) {
        Socio socioExistente = buscarPorId(id);

        socioExistente.setNombre(socioActualizado.getNombre());
        socioExistente.setEmail(socioActualizado.getEmail());

        return socioRepository.save(socioExistente);
    }

    public void eliminar(Long id) {
        Socio socio = buscarPorId(id);

        if (prestamoRepository.countBySocioIdAndEstadoNot(
                id, "DEVUELTO") > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el socio porque tiene préstamos pendientes"
            );
        }

        socioRepository.delete(socio);
    }
}
