package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "vehiculosMS",
        path = "/autofix/vehiculos")
public interface VehiculosFeignClient {
    @GetMapping
    Long getIdVehiculoByPatente(@RequestBody String patente);
}
