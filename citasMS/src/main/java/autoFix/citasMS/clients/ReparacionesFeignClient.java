package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "reparacionesMS",
        path = "/autofix/reparaciones")
public interface ReparacionesFeignClient {

    @GetMapping("/MontoByNombre")
    Integer getMontoReparacionByNombreAndCombustible(@RequestParam String nombre,
                                                            @RequestParam String combustible);
}
