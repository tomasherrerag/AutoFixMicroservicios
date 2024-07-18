package autoFix.reparacionesMS.services;

import autoFix.reparacionesMS.entities.Reparacion;
import autoFix.reparacionesMS.repositories.ReparacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReparacionService {

    ReparacionRepository reparacionRepository;

    public ReparacionService(ReparacionRepository reparacionRepository){
        this.reparacionRepository = reparacionRepository;
    }

    public List<Reparacion> getReparaciones() {return reparacionRepository.findAll();}

    public Reparacion saveReparacion(Reparacion nuevaReparacion) {return reparacionRepository.save(nuevaReparacion);}

    public Reparacion getReparacionByNombre(String nombre){
        return reparacionRepository.findByNombre(nombre);
    }

    public Integer getMontoReparacionByNombreAndCombustible(String nombre, String combustible){
        Reparacion reparacion = reparacionRepository.findByNombre(nombre);
        if (combustible.equals("Gasoline"))
        {
            return reparacion.getPrecioGas();
        }
        if (combustible.equals("Diesel")){
            return reparacion.getPrecioDiesel();
        }
        if (combustible.equals("Hibrid")){
            return reparacion.getPrecioHibrid();
        }
        if (combustible.equals("Electric")){
            return reparacion.getPrecioElectric();
        }
        else {
            throw new RuntimeException("el parámetro de combustible está mal escrito, errfunct: getMontoReparacionByNombreAndCombustible(nombreReparacion: " + nombre + " combustible: " + combustible + ")");
        }
    }

    public Reparacion getReparacionById(int id){
        Optional<Reparacion> optionalReparacion = reparacionRepository.findById(id);
        if (optionalReparacion.isEmpty()){
            throw new RuntimeException("no se encuentra la reparacion, errfunct: getReparacionById");
        }
        return optionalReparacion.get();
    }

    public Reparacion modificarReparacion(int id, Reparacion nuevaReparacion) {
        Optional<Reparacion> optionalReparacionExistente = reparacionRepository.findById(id);
        if (optionalReparacionExistente.isEmpty()) {
            throw new RuntimeException("no se encuentra la reparacion a modificar: errfunct modificarReparacion");
        }
        Reparacion reparacionExistente = optionalReparacionExistente.get();
        reparacionExistente.setNombre(nuevaReparacion.getNombre());
        reparacionExistente.setPrecioDiesel(nuevaReparacion.getPrecioDiesel());
        reparacionExistente.setPrecioGas(nuevaReparacion.getPrecioGas());
        reparacionExistente.setPrecioHibrid(nuevaReparacion.getPrecioHibrid());
        reparacionExistente.setPrecioElectric(nuevaReparacion.getPrecioElectric());
        return reparacionRepository.save(reparacionExistente);
    }

    @Transactional
    public void borrarReparacion(int id){
        reparacionRepository.deleteReparacionById(id);
    }
}
