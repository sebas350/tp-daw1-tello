
package ar.edu.daw1.tpdaw1tello.service;

import ar.edu.daw1.tpdaw1tello.model.Libro;
import ar.edu.daw1.tpdaw1tello.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import ar.edu.daw1.tpdaw1tello.repository.PrestamoRepository;
import ar.edu.daw1.tpdaw1tello.dto.LibroRankingDTO;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    private final PrestamoRepository prestamoRepository;

    public LibroService(
            LibroRepository libroRepository,
            PrestamoRepository prestamoRepository
    ) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
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

    public List<Libro> listarDisponibles() {
        return libroRepository.findAll()
                .stream()
                .filter(libro -> !prestamoRepository
                        .existsByLibroIdAndEstadoNot(libro.getId(), "DEVUELTO"))
                .toList();
    }

    public List<LibroRankingDTO> listarMasPrestados(int top) {
        return libroRepository.findAll()
                .stream()
                .map(libro -> new LibroRankingDTO(
                        libro,
                        prestamoRepository.countByLibroId(libro.getId())
                ))
                .filter(ranking -> ranking.cantidadPrestamos() > 0)
                .sorted((a, b) -> Long.compare(
                        b.cantidadPrestamos(),
                        a.cantidadPrestamos()
                ))
                .limit(top)
                .toList();
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

    public void eliminar(Long id) {
        Libro libro = buscarPorId(id);

        if (prestamoRepository.existsByLibroIdAndEstadoNot(
                id, "DEVUELTO")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el libro porque tiene préstamos pendientes"
            );
        }

        libroRepository.delete(libro);
    }
}
