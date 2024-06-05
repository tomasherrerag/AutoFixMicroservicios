package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "marcasMS",
        path = "/autofix/marcas")
public interface MarcasFeignClient {
}
