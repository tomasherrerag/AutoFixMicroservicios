package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "vehiculosMS",
        path = "/autofix/vehiculos")
public interface VehiculosFeignClient {
}
