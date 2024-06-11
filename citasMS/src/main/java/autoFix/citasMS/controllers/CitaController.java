package autoFix.citasMS.controllers;

import autoFix.citasMS.DTOs.NuevaCitaDTO;
import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.services.CitaService;
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

    @GetMapping("/")
    public ResponseEntity<List<Cita>> listarCitas(){
        List<Cita> listadoCitas = citaService.getCitas();
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
}
