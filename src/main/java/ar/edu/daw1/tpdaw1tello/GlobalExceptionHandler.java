
package ar.edu.daw1.tpdaw1tello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * MANEJO DE ERRORES HTTP 404 (NOT FOUND) Y 409 (El ISBN ya existe)

     */
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail manejarResponseStatusException(
            ResponseStatusException excepcion) {

        HttpStatus estado = HttpStatus.valueOf(
                excepcion.getStatusCode().value()
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                estado,
                excepcion.getReason()
        );

        problemDetail.setTitle(estado.getReasonPhrase());

        return problemDetail;
    }

    /*
     * MANEJO DE ERRORES HTTP 400 BAD REQUEST (campos obligatorios)

     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidaciones(
            MethodArgumentNotValidException excepcion) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Error de validación de los datos"
        );

        problemDetail.setTitle("Bad Request");

        Map<String, String> errores = new HashMap<>();

        excepcion.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        problemDetail.setProperty("errores", errores);

        return problemDetail;
    }

    /*
     * MANEJO DE ERRORES HTTP 400 BAD REQUEST (JSON mal formado)

     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail manejarJsonMalFormado(
            HttpMessageNotReadableException excepcion) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El JSON enviado no es válido"
        );

        problemDetail.setTitle("Bad Request");

        return problemDetail;
    }
}

