
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Socio;
import ar.edu.daw1.tpdaw1tello.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
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

        socioRepository.delete(socio);
    }
}
