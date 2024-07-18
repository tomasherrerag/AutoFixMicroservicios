package autoFix.vehiculosMS.controllers;

import autoFix.vehiculosMS.entities.Vehiculo;
import autoFix.vehiculosMS.services.VehiculoService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autofix/vehiculos")
public class VehiculoController {

    VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService){
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Vehiculo>> listarVehiculos(){
        List<Vehiculo> listadoVehiculos = vehiculoService.getVehiculos();
        return ResponseEntity.ok(listadoVehiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable Long id){
        Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
        return ResponseEntity.ok(vehiculo);
    }

    @PostMapping("/")
    public ResponseEntity<Vehiculo> saveVehiculo(@RequestBody Vehiculo vehiculo){
        Vehiculo nuevoVehiculo = vehiculoService.saveVehiculo(vehiculo);
        return ResponseEntity.ok(nuevoVehiculo);
    }

    @PutMapping("/{id}")
    public Vehiculo updateVehiculo(@PathVariable Long id, @RequestBody Vehiculo nuevoVehiculo){
        return vehiculoService.modificarVehiculo(id, nuevoVehiculo);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehiculo(@PathVariable Long id){
        vehiculoService.borrarVehiculo(id);
        return new ResponseEntity<>("Vehiculo eliminado", HttpStatus.OK);
    }

    @GetMapping("/getIdVehiculoByPatente")
    public ResponseEntity<Long> getIdVehiculoByPatente(@RequestParam String patente){
        Long idVehiculo = this.vehiculoService.getIdVehiculoByPatente(patente);
        return ResponseEntity.ok(idVehiculo);
    }

    @GetMapping("/getIdMarcaByIdVehiculo")
    public ResponseEntity<Integer> getIdMarcaByIdVehiculo(@RequestParam Long idVehiculo){
        int idMarca = this.vehiculoService.getIdMarcaByIdVehiculo(idVehiculo);
        return ResponseEntity.ok(idMarca);
    }

    @GetMapping("/getCombustibleByPatente")
    public ResponseEntity<String> getTipoCombustibleByPatente(@RequestParam String patente){
        String combustible = vehiculoService.getTipoCombustibleByPatente(patente);
        return ResponseEntity.ok(combustible);
    }
}
