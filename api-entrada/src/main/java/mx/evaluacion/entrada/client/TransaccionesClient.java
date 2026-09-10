package mx.evaluacion.entrada.client;

import mx.evaluacion.entrada.dto.OperacionInternaRequest;
import mx.evaluacion.entrada.dto.OperacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "transacciones", url = "${transacciones.url}")
public interface TransaccionesClient {
    @PostMapping("/api/transacciones")
    OperacionResponse crear(@RequestBody OperacionInternaRequest request);
}
