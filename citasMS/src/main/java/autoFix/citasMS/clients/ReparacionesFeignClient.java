package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "reparacionesMS",
        path = "/autofix/reparaciones")
public interface ReparacionesFeignClient {

    @GetMapping("/MontoByNombreAndCombustible")
    Integer getMontoReparacionByNombreAndCombustible(@RequestParam String nombre,
                                                            @RequestParam String combustible);

    @GetMapping("/obtenerNombresReparaciones")
    List<String> obtenerNombresReparaciones();
}
