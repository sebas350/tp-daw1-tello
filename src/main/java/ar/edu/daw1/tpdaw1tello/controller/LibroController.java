
package ar.edu.daw1.tpdaw1tello.controller;

import ar.edu.daw1.tpdaw1tello.model.Libro;
import ar.edu.daw1.tpdaw1tello.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<java.util.List<Libro>> listar() {
        return ResponseEntity.ok(libroService.listar());
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

}
