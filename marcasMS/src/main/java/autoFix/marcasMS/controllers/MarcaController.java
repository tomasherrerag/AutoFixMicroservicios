package autoFix.marcasMS.controllers;

import autoFix.marcasMS.entities.Marca;
import autoFix.marcasMS.services.MarcaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autofix/marcas")
public class MarcaController {

    MarcaService marcaService;

    public MarcaController(MarcaService marcaService){
        this.marcaService = marcaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Marca>> listarMarcas()
    {
        List<Marca> listadoMarcas = marcaService.getMarcas();
        return ResponseEntity.ok(listadoMarcas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> obtenerMarca(@PathVariable int id){
        Marca marca = marcaService.getMarcaById(id);
        return ResponseEntity.ok(marca);
    }

    @PostMapping("/")
    public ResponseEntity<Marca> saveMarca(@RequestBody Marca marca){
        Marca nuevaMarca = marcaService.saveMarca(marca);
        return ResponseEntity.ok(nuevaMarca);
    }

    @PutMapping("/{id}")
    public Marca updateMarca(@PathVariable int id, @RequestBody Marca nuevaMarca){
        return marcaService.modificarMarca(id, nuevaMarca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMarca(@PathVariable int id){
        marcaService.borrarMarca(id);
        return new ResponseEntity<>("marca eliminada", HttpStatus.OK);
    }
}
