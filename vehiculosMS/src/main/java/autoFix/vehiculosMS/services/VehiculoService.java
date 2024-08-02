package autoFix.vehiculosMS.services;

import autoFix.vehiculosMS.Repositories.VehiculoRepository;
import autoFix.vehiculosMS.entities.Vehiculo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository){
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> getVehiculos(){ return vehiculoRepository.findAll();}

    public Vehiculo getVehiculoByPatente(String patente){
        return vehiculoRepository.findVehiculoByPatente(patente);//revisar que pasa si el vehículo no está (probar uso de optional)
    }

    public Long getIdVehiculoByPatente(String patente){
        return vehiculoRepository.findVehiculoByPatente(patente).getId();
    }

    public String getTipoVehiculoByPatente(String patente){
        return vehiculoRepository.findVehiculoByPatente(patente).getTipoVehiculo();
    }

    public int getIdMarcaByIdVehiculo(Long idVehiculo){
        return vehiculoRepository.getById(idVehiculo).getIdMarca();
    }

    public Vehiculo getVehiculoById(Long id){
        Optional<Vehiculo> optionalVehiculo = vehiculoRepository.findById(id);
        if (optionalVehiculo.isEmpty()){
            throw new RuntimeException("No se encuentra el vehiculo entregado, errfunct:getVehiculoById");
        }
        return optionalVehiculo.get();
    }

    public String getTipoCombustibleByPatente(String patente){
        return getVehiculoByPatente(patente).getMotor();
    }

    public Vehiculo saveVehiculo(Vehiculo nuevoVehiculo) {return vehiculoRepository.save(nuevoVehiculo);}

    public Vehiculo modificarVehiculo(Long id, Vehiculo nuevoVehiculo){
        Optional<Vehiculo> optionalVehiculoExistente = vehiculoRepository.findById(id);
        if (optionalVehiculoExistente.isEmpty()){
            throw new RuntimeException("no se encuentra el vehiculo a modificar, errfunct: modificarVehiculo");
        }
        Vehiculo vehiculoExistente = optionalVehiculoExistente.get();
        vehiculoExistente.setPatente(nuevoVehiculo.getPatente());
        vehiculoExistente.setTipoVehiculo(nuevoVehiculo.getTipoVehiculo());
        vehiculoExistente.setAsientos(nuevoVehiculo.getAsientos());
        vehiculoExistente.setModelo(nuevoVehiculo.getModelo());
        vehiculoExistente.setMotor(nuevoVehiculo.getMotor());
        vehiculoExistente.setFabricYear(nuevoVehiculo.getFabricYear());
        vehiculoExistente.setIdMarca(nuevoVehiculo.getIdMarca());
        return vehiculoRepository.save(vehiculoExistente);
    }

    public void borrarVehiculo(Long id){
        vehiculoRepository.deleteVehiculoById(id);
    }
}
