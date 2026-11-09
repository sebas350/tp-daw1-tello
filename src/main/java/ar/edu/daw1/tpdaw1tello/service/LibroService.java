
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Libro;
import ar.edu.daw1.tpdaw1tello.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro crear(Libro libro) {

        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un libro con ese ISBN"
            );
        }

        return libroRepository.save(libro);
    }
    public List<Libro> listar() {
        return libroRepository.findAll();
    }

    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Libro no encontrado"
                ));
    }
    public Libro actualizar(Long id, Libro libroActualizado) {
        Libro libroExistente = buscarPorId(id);

        if (libroRepository.existsByIsbn(libroActualizado.getIsbn())
                && !libroExistente.getIsbn().equals(libroActualizado.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un libro con ese ISBN"
            );
        }

        libroExistente.setTitulo(libroActualizado.getTitulo());
        libroExistente.setAutor(libroActualizado.getAutor());
        libroExistente.setIsbn(libroActualizado.getIsbn());

        return libroRepository.save(libroExistente);
    }
}
