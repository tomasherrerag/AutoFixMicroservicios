package autoFix.citasMS.controllers;

import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.services.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
