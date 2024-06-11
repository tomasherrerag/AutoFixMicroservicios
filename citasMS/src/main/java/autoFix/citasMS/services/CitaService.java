package autoFix.citasMS.services;

import autoFix.citasMS.clients.MarcasFeignClient;
import autoFix.citasMS.clients.ReparacionesFeignClient;
import autoFix.citasMS.clients.VehiculosFeignClient;
import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.repositories.CitaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Transactional
    public Cita nuevaCita(String patenteVehiculo, List<String> listaReparaciones, int kilometraje, int bono){
        Cita nuevaCita = new Cita();
        // datos utiles
        Long idVehiculo = vehiculosFeignClient.getIdVehiculoByPatente(patenteVehiculo);

        //GetIdMarcaByIdVehiculoRequest getIdMarcaByIdVehiculoRequest = new GetIdMarcaByIdVehiculoRequest(idVehiculo);
        int idMarca = vehiculosFeignClient.getIdMarcaByIdVehiculo(idVehiculo);

        //set datos cita
        nuevaCita.setIdVehiculo(idVehiculo);
        nuevaCita.setNombresReparaciones(String.join(",", listaReparaciones));
        nuevaCita.setKilometraje(kilometraje);
        nuevaCita.setFechaIn(LocalDateTime.now());
        nuevaCita.setBono(0);
        if (bono == 1)
        {
            int numeroBonos = marcasFeignClient.getNumBonosByIdMarca(idMarca);
            if (numeroBonos > 0)
            {
                nuevaCita.setBono(marcasFeignClient.getMontoBonoByIdMarca(idMarca));
                marcasFeignClient.descontarUnBonoByIdMarca(idMarca);
            }
        }
        return citaRepository.save(nuevaCita);
    }

    public void borrarCitaById(Long id){
        citaRepository.deleteById(id);
    }

    @Transactional
    public Cita reparacionesListas(Long idCita){
        Optional<Cita> optionalCita = citaRepository.findById(idCita);
        if (optionalCita.isEmpty()){
            throw new RuntimeException("no se encuentra la cita a modificar");
        }
        Cita citaOrigen = optionalCita.get();
        citaOrigen.setFechaReady(LocalDateTime.now());
        return citaRepository.save(citaOrigen);
    }


}
