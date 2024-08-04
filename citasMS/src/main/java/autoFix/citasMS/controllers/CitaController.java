package autoFix.citasMS.controllers;

import autoFix.citasMS.DTOs.NuevaCitaDTO;
import autoFix.citasMS.DTOs.NuevaCitaUnitariaDTO;
import autoFix.citasMS.DTOs.Reporte1Reparacion;
import autoFix.citasMS.DTOs.Reporte2Reparacion;
import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.entities.CitaUnitaria;
import autoFix.citasMS.services.CitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autofix/citas")
public class CitaController {

    CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    //Bloque Citas

    //Bloque  Citas - GetMapping

    @GetMapping("/")
    public ResponseEntity<List<Cita>> listarCitas(){
        List<Cita> listadoCitas = citaService.getCitas();
        return ResponseEntity.ok(listadoCitas);
    }

    @GetMapping("/Cerradas")
    public ResponseEntity<List<Cita>> listarCitasCerradas(){
        List<Cita> listadoCitas = citaService.obtenerCitasCerradas();
        return ResponseEntity.ok(listadoCitas);
    }

    @GetMapping("/ARetirar")
    public ResponseEntity<List<Cita>> listarCitasARetirar(){
        List<Cita> listadoCitas = citaService.obtenerCiasARetirar();
        return ResponseEntity.ok(listadoCitas);
    }

    @GetMapping("/byPatente/{patente}")
    public ResponseEntity<List<Cita>> listarCitasByPatente(@PathVariable String patente){
        List<Cita> listadoCitas = citaService.getCitasByPatente(patente);
        return ResponseEntity.ok(listadoCitas);
    }

    @GetMapping("/reportes/reporte1")
    public ResponseEntity<List<Reporte1Reparacion>> generarReporte1(@RequestParam int mes, @RequestParam int year){
        List<Reporte1Reparacion> reporte1 = citaService.generarReporte1(mes, year);
        return ResponseEntity.ok(reporte1);
    }

    @GetMapping("/reportes/reporte2")
    public ResponseEntity<List<Reporte2Reparacion>> generarReporte2(@RequestParam int mes, @RequestParam int year){
        List<Reporte2Reparacion> reporte2 = citaService.generarReporte2(mes, year);
        return ResponseEntity.ok(reporte2);
    }


    //Bloque  Citas - PostMapping

    @PostMapping("/in/")
    public ResponseEntity<Cita> nuevaCita(@RequestBody NuevaCitaDTO nuevaCitaDto){
        Cita nuevaCita = citaService.nuevaCita(nuevaCitaDto.getPatente(), nuevaCitaDto.getListaReparaciones(), nuevaCitaDto.getKilometraje(), nuevaCitaDto.getBono());
        return ResponseEntity.ok(nuevaCita);
    }



    //Bloque  Citas - PutMapping

    /*@PutMapping("/ready/{id}")
    public Cita citaReady(@PathVariable Long id){
        return citaService.reparacionesListas(id);
    }

    @PutMapping("/ready/{id}")
    public ResponseEntity<?> citaReady(@PathVariable Long id) {
        try {
            Cita cita = citaService.reparacionesListas(id);
            return ResponseEntity.ok(cita);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }*/

    @PutMapping("/ready/{id}")
    public ResponseEntity<?> citaReady(@PathVariable Long id) {
        int result = citaService.reparacionesListas(id);
        switch(result) {
            case 1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra la cita a modificar.");
            case 2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quedan citas unitarias por terminar.");
            case 0:
                return ResponseEntity.ok("Cita cerrada correctamente.");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error desconocido.");
        }
    }


    @PutMapping("/out/{id}")
    public Cita citaOut(@PathVariable Long id){ return citaService.retiroCita(id); }


    //Bloque Citas - DeleteMapping

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> borrarCita(@PathVariable Long id){
        citaService.borrarCitaById(id);
        return new ResponseEntity<>("Cita eliminada.", HttpStatus.OK);
    }



    //----------------------------------------------------------------------------------------------------------
    //Bloque CitasUnitarias



    //bloque CitasUnitarias - GetMapping

    @GetMapping("/Unitarias/")
    public ResponseEntity<List<CitaUnitaria>> listarCitasUnitarias(){
        List<CitaUnitaria> listadoUnitarias = citaService.getCitasUnitarias();
        return ResponseEntity.ok(listadoUnitarias);
    }

    @GetMapping("/Unitarias/NoListas")
    public ResponseEntity<List<CitaUnitaria>> listarCitasUnitariasNoListas(){
        List<CitaUnitaria> listadoUnitarias = citaService.obtenerCitasUnitariasNoListas();
        return ResponseEntity.ok(listadoUnitarias);
    }

    @GetMapping("/Unitarias/{id}")
    public ResponseEntity<CitaUnitaria> getCitaUnitaria(@PathVariable Long id){
        CitaUnitaria citaUnitaria = citaService.getCitaUnitariaById(id);
        return ResponseEntity.ok(citaUnitaria);
    }

    @GetMapping("/Unitarias/ByPadre{idPadre}")
    public ResponseEntity<List<CitaUnitaria>> listarCitasUnitariasByPadre(@PathVariable Long idPadre){
        List<CitaUnitaria> listadoUnitarias = citaService.getCitasUnitariasByIdCitaPadre(idPadre);
        return ResponseEntity.ok(listadoUnitarias);
    }

    @GetMapping("/Unitarias/ByPatente{patente}")
    public ResponseEntity<List<CitaUnitaria>> listarCitasUnitariasByPatente(@PathVariable String patente){
        List<CitaUnitaria> listadoUnitarias = citaService.getCitasUnitariasByPatente(patente);
        return ResponseEntity.ok(listadoUnitarias);
    }



    //bloque CitasUnitarias - PostMapping

    @PostMapping("/Unitarias/")
    public ResponseEntity<CitaUnitaria> crearCitaUnitaria(@RequestBody NuevaCitaUnitariaDTO nuevaCitaUnitariaDTO){
        CitaUnitaria citaUnitaria = citaService.nuevaCitaUnitaria(nuevaCitaUnitariaDTO);
        return ResponseEntity.ok(citaUnitaria);
    }



    //bloque CitasUnitarias - PutMapping

    @PutMapping("/Unitarias/ready/{id}")
    public ResponseEntity<CitaUnitaria> citaUnitariaReady(@PathVariable Long id){
        CitaUnitaria citaUnitaria = citaService.citaUnitariaReadyById(id);
        return ResponseEntity.ok(citaUnitaria);
    }



    //bloque CitasUnitarias - DeleteMapping

    @DeleteMapping("/Unitarias/delete/{id}")
    public ResponseEntity<String> borrarCitaUnitaria(@PathVariable Long id){
        citaService.borrarCitaUnitariaById(id);
        return new ResponseEntity<>("Cita Unitaria eliminada.", HttpStatus.OK);
    }

    /*@DeleteMapping("/Unitarias/deleteByIdPadreAndReparacion")
    public ResponseEntity<String> borrarCitaUnitariaByIdPadreAndReparacion(@RequestParam Long idPadre, @RequestParam String reparacion){
        citaService.borrarCitaUnitariaByIdPadreAndReparacion(idPadre, reparacion);
        return new ResponseEntity<>("Cita Unitaria eliminada.", HttpStatus.OK);
    }*/

    @DeleteMapping("/Unitarias/deleteByIdPadreAndReparacion")
    public ResponseEntity<String> borrarCitaUnitariaByIdPadreAndReparacion(@RequestParam Long idPadre, @RequestParam String reparacion){
        boolean resultado = citaService.borrarCitaUnitariaByIdPadreAndReparacion(idPadre, reparacion);
        if (resultado) {
            return new ResponseEntity<>("Cita Unitaria eliminada.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al eliminar la Cita Unitaria.", HttpStatus.BAD_REQUEST);
        }
    }

}
