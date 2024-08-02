package autoFix.citasMS.clients;

import autoFix.citasMS.DTOs.Vehiculo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "vehiculosMS",
        path = "/autofix/vehiculos")
public interface VehiculosFeignClient {

    @GetMapping("/getIdVehiculoByPatente")
    Long getIdVehiculoByPatente(@RequestParam String patente);

    @GetMapping("/getIdMarcaByIdVehiculo")
    Integer getIdMarcaByIdVehiculo(@RequestParam Long idVehiculo);

    @GetMapping("/getCombustibleByPatente")
    String getTipoCombustibleByPatente(@RequestParam String patente);

    @GetMapping("/getPatenteByIdVehiculo{id}")
    String getPatenteByIdVehiculo(@PathVariable Long id);

    @GetMapping("/{id}")
    Vehiculo getVehiculoById(@PathVariable Long id);

    @GetMapping("/getTipoVehiculoByPatente")
    String getTipoVehiculoByPatente(@RequestParam String patente);
}
