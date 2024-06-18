package autoFix.citasMS.controllers;

import autoFix.citasMS.DTOs.NuevaCitaDTO;
import autoFix.citasMS.DTOs.NuevaCitaUnitariaDTO;
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

    @GetMapping("/")
    public ResponseEntity<List<Cita>> listarCitas(){
        List<Cita> listadoCitas = citaService.getCitas();
        return ResponseEntity.ok(listadoCitas);
    }

    @GetMapping("/byPatente/{patente}")
    public ResponseEntity<List<Cita>> listarCitasByPatente(@PathVariable String patente){
        List<Cita> listadoCitas = citaService.getCitasByPatente(patente);
        return ResponseEntity.ok(listadoCitas);
    }

    @PostMapping("/in/")
    public ResponseEntity<Cita> nuevaCita(@RequestBody NuevaCitaDTO nuevaCitaDto){
        Cita nuevaCita = citaService.nuevaCita(nuevaCitaDto.getPatente(), nuevaCitaDto.getListaReparaciones(), nuevaCitaDto.getKilometraje(), nuevaCitaDto.getBono());
        return ResponseEntity.ok(nuevaCita);
    }

    @PutMapping("/ready/{id}")
    public Cita citaReady(@PathVariable Long id){
        return citaService.reparacionesListas(id);
    }

    //Bloque CitasUnitarias

    @GetMapping("/Unitarias/")
    public ResponseEntity<List<CitaUnitaria>> listarCitasUnitarias(){
        List<CitaUnitaria> listadoUnitarias = citaService.getCitasUnitarias();
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

    //bloque PostMapping

    @PostMapping("/Unitarias/")
    public ResponseEntity<CitaUnitaria> crearCitaUnitaria(@RequestBody NuevaCitaUnitariaDTO nuevaCitaUnitariaDTO){
        CitaUnitaria citaUnitaria = citaService.nuevaCitaUnitaria(nuevaCitaUnitariaDTO);
        return ResponseEntity.ok(citaUnitaria);
    }

    //bloque PutMapping
    @PutMapping("/Unitarias/ready/{id}")
    public ResponseEntity<CitaUnitaria> citaUnitariaReady(@PathVariable Long id){
        CitaUnitaria citaUnitaria = citaService.citaUnitariaReadyById(id);
        return ResponseEntity.ok(citaUnitaria);
    }

    //bloque DeleteMapping
    @DeleteMapping("/Unitarias/delete/{id}")
    public ResponseEntity<String> borrarCitaUnitaria(@PathVariable Long id){
        citaService.borrarCitaUnitariaById(id);
        return new ResponseEntity<>("Cita Unitaria eliminada.", HttpStatus.OK);
    }
}
