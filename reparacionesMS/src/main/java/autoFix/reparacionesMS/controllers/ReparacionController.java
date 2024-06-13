package autoFix.reparacionesMS.controllers;

import autoFix.reparacionesMS.entities.Reparacion;
import autoFix.reparacionesMS.services.ReparacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/autofix/reparaciones")
public class ReparacionController {

    ReparacionService reparacionService;

    public ReparacionController(ReparacionService reparacionService){
        this.reparacionService = reparacionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Reparacion>> listarReparaciones(){
        List<Reparacion> listadoReparaciones = reparacionService.getReparaciones();
        return ResponseEntity.ok(listadoReparaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reparacion> obtenerReparacion(@PathVariable int id){
        Reparacion reparacion = reparacionService.getReparacionById(id);
        return ResponseEntity.ok(reparacion);
    }

    @PostMapping("/")
    public ResponseEntity<Reparacion> saveReparacion(@RequestBody Reparacion reparacion)
    {
        Reparacion nuevaReparacion = reparacionService.saveReparacion(reparacion);
        return ResponseEntity.ok(nuevaReparacion);
    }

    @PutMapping("/{id}")
    public Reparacion updateReparacion(@PathVariable int id, @RequestBody Reparacion nuevaReparacion){
        return reparacionService.modificarReparacion(id, nuevaReparacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMarca(@PathVariable int id){
        reparacionService.borrarReparacion(id);
        return new ResponseEntity<>("Reparacion eliminada", HttpStatus.OK);
    }

    @GetMapping("/MontoByNombre")
    public ResponseEntity<Integer> getMontoReparacionByNombreAndCombustible(@RequestParam String nombre,
                                                                            @RequestParam String combustible){
        Integer montoReparacion = reparacionService.getMontoReparacionByNombreAndCombustible(nombre, combustible);
        return ResponseEntity.ok(montoReparacion);
    }
}
