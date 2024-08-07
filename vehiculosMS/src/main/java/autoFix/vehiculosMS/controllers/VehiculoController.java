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


    //bloque GetMapping

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

    @GetMapping("/getPatenteByIdVehiculo{id}")
    public ResponseEntity<String> getPatenteByIdVehiculo(@PathVariable Long id){
        Vehiculo vehiculo = vehiculoService.getVehiculoById(id);
        return ResponseEntity.ok(vehiculo.getPatente());
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

    @GetMapping("/getTipoVehiculoByPatente")
    public ResponseEntity<String> getTipoVehiculoByPatente(@RequestParam String patente){
        String tipo = vehiculoService.getTipoVehiculoByPatente(patente);
        return ResponseEntity.ok(tipo);
    }


    // Bloque PostMapping

    @PostMapping("/")
    public ResponseEntity<Vehiculo> saveVehiculo(@RequestBody Vehiculo vehiculo){
        Vehiculo nuevoVehiculo = vehiculoService.saveVehiculo(vehiculo);
        return ResponseEntity.ok(nuevoVehiculo);
    }




    //Bloque PutMapping

    @PutMapping("/{id}")
    public Vehiculo updateVehiculo(@PathVariable Long id, @RequestBody Vehiculo nuevoVehiculo){
        return vehiculoService.modificarVehiculo(id, nuevoVehiculo);
    }




    //Bloque DeleteMapping

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehiculo(@PathVariable Long id){
        vehiculoService.borrarVehiculo(id);
        return new ResponseEntity<>("Vehiculo eliminado", HttpStatus.OK);
    }



}
