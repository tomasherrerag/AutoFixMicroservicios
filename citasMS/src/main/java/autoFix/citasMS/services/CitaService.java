package autoFix.citasMS.services;

import autoFix.citasMS.DTOs.NuevaCitaDTO;
import autoFix.citasMS.DTOs.NuevaCitaUnitariaDTO;
import autoFix.citasMS.clients.MarcasFeignClient;
import autoFix.citasMS.clients.ReparacionesFeignClient;
import autoFix.citasMS.clients.VehiculosFeignClient;
import autoFix.citasMS.entities.Cita;
import autoFix.citasMS.entities.CitaUnitaria;
import autoFix.citasMS.repositories.CitaRepository;
import autoFix.citasMS.repositories.CitaUnitariaRepository;
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
    CitaUnitariaRepository citaUnitariaRepository;

    @Autowired
    public CitaService(CitaRepository citaRepository,
                       MarcasFeignClient marcasFeignClient,
                       ReparacionesFeignClient reparacionesFeignClient,
                       VehiculosFeignClient vehiculosFeignClient,
                       CitaUnitariaRepository citaUnitariaRepository)
    {
        this.citaRepository = citaRepository;
        this.marcasFeignClient = marcasFeignClient;
        this.reparacionesFeignClient = reparacionesFeignClient;
        this.vehiculosFeignClient = vehiculosFeignClient;
        this.citaUnitariaRepository = citaUnitariaRepository;
    }

    //Bloque Citas

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


    //Bloque citasUnitariasService

    //para listar todas las citas unitarias, utilidad debug mas que nada
    public List<CitaUnitaria> getCitasUnitarias() {
        return citaUnitariaRepository.findAll();
    }

    //para listar todas las citas unitarias pertenecientes a una cita, utilizable para desplegar en frontend
    public List<CitaUnitaria> getCitasUnitariasByIdCitaPadre(Long idCitaPadre) {
        return citaUnitariaRepository.findCitaUnitariasByIdCitaPadre(idCitaPadre);
    }

    //listar todas las citas unitarias de un auto, util para calculo de monto final
    public List<CitaUnitaria> getCitasUnitariasByPatente(String patente){
        return citaUnitariaRepository.findCitaUnitariasByPatente(patente);
    }

    //para obtener una cita en especifico, necesaria para modificar citas unitarias (front y back)
    public CitaUnitaria getCitaUnitariaById(Long idCitaUnitaria) {
        Optional<CitaUnitaria> citaUnitaria = citaUnitariaRepository.findById(idCitaUnitaria);
        if (citaUnitaria.isPresent()) {
            return citaUnitaria.get();
        }
        throw new RuntimeException("No se encuentra la cita unitaria con el id especificado, errfunct: getCitaUnitariaById");
    }

    //creación de una cita unitaria, para mejor desempeño, crearlas en base al set de reparaciones en string
    @Transactional
    public CitaUnitaria nuevaCitaUnitaria(NuevaCitaUnitariaDTO nuevaCitaUnitariaDTO){
        CitaUnitaria nuevaCitaUnitaria = new CitaUnitaria();
        nuevaCitaUnitaria.setIdCitaPadre(nuevaCitaUnitariaDTO.getIdPadre());
        nuevaCitaUnitaria.setPatente(nuevaCitaUnitariaDTO.getPatente());
        String combustible = vehiculosFeignClient.getTipoCombustibleByPatente(nuevaCitaUnitaria.getPatente());
        nuevaCitaUnitaria.setReparacion(nuevaCitaUnitariaDTO.getReparacion());
        nuevaCitaUnitaria.setMontoReparacion(reparacionesFeignClient.getMontoReparacionByNombreAndCombustible(nuevaCitaUnitariaDTO.getReparacion(), combustible));
        return citaUnitariaRepository.save(nuevaCitaUnitaria);
    }

    //Para establecer que una cita Unitaria ha sido resuelta
    @Transactional
    public CitaUnitaria citaUnitariaReadyById(Long id){
        CitaUnitaria citaUnitaria = getCitaUnitariaById(id);
        citaUnitaria.setFechaReparacion(LocalDateTime.now());
        return citaUnitariaRepository.save(citaUnitaria);
    }

    //borrar una citaUnitaria, debe utilizarse al borrar una cita mayor
    public void borrarCitaUnitariaById(Long id){
        citaUnitariaRepository.deleteById(id);
    }

}
