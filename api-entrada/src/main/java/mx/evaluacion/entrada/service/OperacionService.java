package mx.evaluacion.entrada.service;

import mx.evaluacion.entrada.client.TransaccionesClient;
import mx.evaluacion.entrada.dto.OperacionInternaRequest;
import mx.evaluacion.entrada.dto.OperacionRequest;
import mx.evaluacion.entrada.dto.OperacionResponse;
import org.springframework.stereotype.Service;

@Service
public class OperacionService {
    private final AesService aesService;
    private final TransaccionesClient transaccionesClient;

    public OperacionService(AesService aesService, TransaccionesClient transaccionesClient) {
        this.aesService = aesService;
        this.transaccionesClient = transaccionesClient;
    }

    public OperacionResponse crear(OperacionRequest request) {
        String secretoPlano = aesService.descifrar(request.secreto());
        var destino = new OperacionInternaRequest(request.operacion(), request.importe(), request.cliente(), secretoPlano);
        return transaccionesClient.crear(destino);
    }
}
