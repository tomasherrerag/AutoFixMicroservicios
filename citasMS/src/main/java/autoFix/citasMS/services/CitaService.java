package autoFix.citasMS.services;

import autoFix.citasMS.clients.MarcasFeignClient;
import autoFix.citasMS.clients.ReparacionesFeignClient;
import autoFix.citasMS.clients.VehiculosFeignClient;
import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.repositories.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    CitaRepository citaRepository;
    MarcasFeignClient marcasFeignClient;
    ReparacionesFeignClient reparacionesFeignClient;
    VehiculosFeignClient vehiculosFeignClient;

    @Autowired
    public CitaService(CitaRepository citaRepository,
                       MarcasFeignClient marcasFeignClient,
                       ReparacionesFeignClient reparacionesFeignClient,
                       VehiculosFeignClient vehiculosFeignClient)
    {
        this.citaRepository = citaRepository;
        this.marcasFeignClient = marcasFeignClient;
        this.reparacionesFeignClient = reparacionesFeignClient;
        this.vehiculosFeignClient = vehiculosFeignClient;
    }

    public List<Cita> getCitas(){
        return citaRepository.findAllByOrderByFechaInAsc();
    }

    public Cita getCitaById(Long id){
        Optional<Cita> optionalCita = citaRepository.findById(id);
        if (optionalCita.isEmpty()){
            throw new RuntimeException("no se encuentra una cita con el id correspondiente");
        }
        return optionalCita.get();
    }

    public List<Cita> getCitasByPatente(String patente){
        Long vehiculoid = vehiculosFeignClient.getIdVehiculoByPatente(patente);
        return citaRepository.getCitasByIdVehiculoOrderByFechaInAsc(vehiculoid);
    }
}
