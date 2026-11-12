
package ar.edu.daw1.tpdaw1tello.controller;

import ar.edu.daw1.tpdaw1tello.model.Libro;
import ar.edu.daw1.tpdaw1tello.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ar.edu.daw1.tpdaw1tello.dto.LibroRankingDTO;

import java.net.URI;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@Valid @RequestBody Libro libro) {

        Libro libroCreado = libroService.crear(libro);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(libroCreado.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(libroCreado);
    }

    @GetMapping
    public ResponseEntity<java.util.List<Libro>> listar(
            @RequestParam(required = false) Boolean disponible) {

        if (Boolean.TRUE.equals(disponible)) {
            return ResponseEntity.ok(libroService.listarDisponibles());
        }

        return ResponseEntity.ok(libroService.listar());
    }

    @GetMapping("/mas-prestados")
    public ResponseEntity<java.util.List<LibroRankingDTO>> listarMasPrestados(
            @RequestParam(defaultValue = "5") int top) {

        return ResponseEntity.ok(libroService.listarMasPrestados(top));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Libro libro) {

        Libro libroActualizado = libroService.actualizar(id, libro);

        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        libroService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
