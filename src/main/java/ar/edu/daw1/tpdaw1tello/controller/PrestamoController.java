
package ar.edu.daw1.tpdaw1tello.controller;

import ar.edu.daw1.tpdaw1tello.model.Prestamo;
import ar.edu.daw1.tpdaw1tello.service.PrestamoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(
            @Valid @RequestBody Prestamo prestamo) {

        Prestamo prestamoCreado = prestamoService.crear(prestamo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prestamoCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(prestamoCreado);
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> listar(
            @RequestParam(required = false) String estado) {

        if (estado != null) {
            return ResponseEntity.ok(
                    prestamoService.listarPorEstado(estado)
            );
        }

        return ResponseEntity.ok(prestamoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Prestamo> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {

        String estado = datos.get("estado");

        if (estado == null || estado.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el estado"
            );
        }

        return ResponseEntity.ok(
                prestamoService.cambiarEstado(id, estado)
        );
    }

    @PatchMapping("/{id}/pagar-multa")
    public ResponseEntity<Prestamo> pagarMulta(@PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.pagarMulta(id)
        );
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<Prestamo> renovar(@PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.renovar(id)
        );
    }
}
