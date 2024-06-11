package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "vehiculosMS",
        path = "/autofix/vehiculos")
public interface VehiculosFeignClient {

    @GetMapping("/getIdVehiculoByPatente")
    Long getIdVehiculoByPatente(@RequestParam String patente);

    @GetMapping("/getIdMarcaByIdVehiculo")
    Integer getIdMarcaByIdVehiculo(@RequestParam Long idVehiculo);
}
