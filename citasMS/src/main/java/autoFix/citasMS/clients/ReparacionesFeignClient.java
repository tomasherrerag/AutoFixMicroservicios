package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "reparacionesMS",
        path = "/autofix/reparaciones")
public interface ReparacionesFeignClient {
}
