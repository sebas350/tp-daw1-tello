
package ar.edu.daw1.tpdaw1tello.controller;

import ar.edu.daw1.tpdaw1tello.model.Socio;
import ar.edu.daw1.tpdaw1tello.service.SocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ar.edu.daw1.tpdaw1tello.model.Prestamo;
import ar.edu.daw1.tpdaw1tello.service.PrestamoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;
    private final PrestamoService prestamoService;

    public SocioController(
            SocioService socioService,
            PrestamoService prestamoService) {

        this.socioService = socioService;
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Socio> crear(@Valid @RequestBody Socio socio) {
        Socio socioCreado = socioService.crear(socio);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(socioCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(socioCreado);
    }

    @GetMapping
    public ResponseEntity<List<Socio>> listar() {
        return ResponseEntity.ok(socioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.buscarPorId(id));
    }
    @GetMapping("/{id}/prestamos")
    public ResponseEntity<List<Prestamo>> listarPrestamos(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.listarPorSocio(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Socio socio) {

        Socio socioActualizado = socioService.actualizar(id, socio);

        return ResponseEntity.ok(socioActualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        socioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
