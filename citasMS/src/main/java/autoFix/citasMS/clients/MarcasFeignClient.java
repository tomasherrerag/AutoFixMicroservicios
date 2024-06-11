package autoFix.citasMS.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "marcasMS",
        path = "/autofix/marcas")
public interface MarcasFeignClient {

    @GetMapping("/getNumBonosByIdMarca")
    Integer getNumBonosByIdMarca(@RequestParam int idMarca);

    @GetMapping("/getMontoBonoByIdMarca")
    Integer getMontoBonoByIdMarca(@RequestParam int idMarca);

    @PutMapping("/descontarUnBonoByIdMarca")
    void descontarUnBonoByIdMarca(@RequestParam int idMarca);
}
