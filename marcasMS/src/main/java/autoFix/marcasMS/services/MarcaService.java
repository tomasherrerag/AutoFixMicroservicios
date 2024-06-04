package autoFix.marcasMS.services;

import autoFix.marcasMS.entities.Marca;
import autoFix.marcasMS.repositories.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository){
        this.marcaRepository = marcaRepository;
    }

    public List<Marca> getMarcas() {
        return marcaRepository.findAllByOrderByNombreAsc();
    }

    public Marca saveMarca(Marca nuevaMarca) { return marcaRepository.save(nuevaMarca);}

    public Marca getMarcaByName(String nombre){
        return marcaRepository.findByNombre(nombre);
    }

    public int getMontoById(int idMarca){
        return marcaRepository.findById(idMarca).get().getMontoBono();
    }

    public Marca getMarcaById(int id){
        Optional<Marca> optionalMarca = marcaRepository.findById(id);
        if (optionalMarca.isEmpty()){
            throw new RuntimeException("no se encontró la marca especificada, errfunct: getMarcaById");
        }
        return optionalMarca.get();
    }

    public Marca modificarMarca(int id, Marca nuevaMarca){
        Optional<Marca> optionalMarcaExistente = marcaRepository.findById(id);
        if (optionalMarcaExistente.isEmpty()){throw new RuntimeException("no se encuenta la marca a editar, errfunct:modificarMarca");}
        Marca marcaExistente = optionalMarcaExistente.get();
        marcaExistente.setNombre(marcaExistente.getNombre());
        marcaExistente.setNumBonos(nuevaMarca.getNumBonos());
        marcaExistente.setMontoBono(nuevaMarca.getMontoBono());
        return marcaRepository.save(marcaExistente);
    }

    public void borrarMarca(int id){
        marcaRepository.deleteMarcaById(id);
    }
}
