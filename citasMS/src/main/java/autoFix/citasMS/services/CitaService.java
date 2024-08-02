package autoFix.citasMS.services;

import autoFix.citasMS.DTOs.NuevaCitaUnitariaDTO;
import autoFix.citasMS.DTOs.Reporte1Reparacion;
import autoFix.citasMS.DTOs.Reporte2Reparacion;
import autoFix.citasMS.DTOs.Vehiculo;
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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

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
        return citaRepository.findAllByOrderByFechaEntradaAsc();
    }

    public List<Cita> obtenerCitasCerradas(){
        return citaRepository.getCitasByFechaReadyIsNull();
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
        return citaRepository.getCitasByIdVehiculoOrderByFechaEntradaAsc(vehiculoid);
    }

    @Transactional
    public Cita nuevaCita(String patenteVehiculo, List<String> listaReparaciones, int kilometraje, int bono){
        Cita nuevaCita = new Cita();
        Cita citaGuardada;
        // datos utiles
        Long idVehiculo = vehiculosFeignClient.getIdVehiculoByPatente(patenteVehiculo);
        String patente;
        // inicializacion datos CitasUnitarias
        NuevaCitaUnitariaDTO citaUnitariaDTO = new NuevaCitaUnitariaDTO();

        //GetIdMarcaByIdVehiculoRequest getIdMarcaByIdVehiculoRequest = new GetIdMarcaByIdVehiculoRequest(idVehiculo);
        int idMarca = vehiculosFeignClient.getIdMarcaByIdVehiculo(idVehiculo);

        //set datos cita
        nuevaCita.setIdVehiculo(idVehiculo);
        nuevaCita.setNombresReparaciones(String.join(",", listaReparaciones));
        nuevaCita.setKilometraje(kilometraje);
        nuevaCita.setFechaEntrada(LocalDateTime.now());
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
        Cita retorno = citaRepository.save(nuevaCita);
        citaGuardada = citaRepository.getCitaByIdVehiculoAndFechaEntrada(nuevaCita.getIdVehiculo(), nuevaCita.getFechaEntrada());

        patente = vehiculosFeignClient.getPatenteByIdVehiculo(idVehiculo);

        citaUnitariaDTO.setIdPadre(citaGuardada.getId());
        citaUnitariaDTO.setPatente(patente);
        for (String reparacion : listaReparaciones){
            citaUnitariaDTO.setReparacion(reparacion);
            nuevaCitaUnitaria(citaUnitariaDTO);
        }

        return retorno;
    }

    public void borrarCitaById(Long id){
        List<CitaUnitaria> listaCitasHijo = getCitasUnitariasByIdCitaPadre(id);
        citaUnitariaRepository.deleteAll(listaCitasHijo);
        citaRepository.deleteById(id);
    }

    @Transactional
    public Cita reparacionesListas(Long idCita){
        Optional<Cita> optionalCita = citaRepository.findById(idCita);
        if (optionalCita.isEmpty()){
            throw new RuntimeException("no se encuentra la cita a modificar, errfunct: reparacionesListas");
        }
        Cita citaOrigen = optionalCita.get();
        if (citaUnitariaRepository.existsCitaUnitariaByIdCitaPadreAndFechaReparacionIsNull(citaOrigen.getId()))
        {
            throw new RuntimeException("quedan citas unitarias por terminar");
        }
        citaOrigen.setFechaReady(LocalDateTime.now());
        return citaRepository.save(citaOrigen);
    }

    @Transactional
    public Cita retiroCita(Long idCita){
        Optional<Cita> optionalCita = citaRepository.findById(idCita);
        if (optionalCita.isEmpty()){
            throw new RuntimeException("no se encuentra la cita a modificar, errfunct: retiroCita");
        }
        Cita citaOrigen = optionalCita.get();
        citaOrigen.setFechaOut(LocalDateTime.now());

        //obtener datos vehiculo
        Vehiculo vehiculo = vehiculosFeignClient.getVehiculoById(citaOrigen.getIdVehiculo());

        //calculo aumento kilometraje
        int kilometraje = citaOrigen.getKilometraje();
        double recargaKilometraje;
        if ( kilometraje < 5001){
            recargaKilometraje = 0;
        }
        else if (kilometraje < 12001)
        {
            if (Objects.equals(vehiculo.getTipoVehiculo(), "Sedan") || Objects.equals(vehiculo.getTipoVehiculo(), "Hatchback"))
            {
                recargaKilometraje = citaOrigen.getMontoBase() * 0.03;
            }
            else
            {
                recargaKilometraje = citaOrigen.getMontoBase() * 0.05;
            }
        }
        else if (kilometraje < 25001)
        {
            if (Objects.equals(vehiculo.getTipoVehiculo(), "Sedan") || Objects.equals(vehiculo.getTipoVehiculo(), "Hatchback"))
            {
                recargaKilometraje = citaOrigen.getMontoBase() * 0.07;
            }
            else
            {
                recargaKilometraje = citaOrigen.getMontoBase() * 0.09;
            }
        }
        else if (kilometraje < 40001)
        {
            recargaKilometraje = citaOrigen.getMontoBase() * 0.12;
        }
        else
        {
            recargaKilometraje = citaOrigen.getMontoBase() * 0.2;
        }

        //calculo recargo antique
        double recargoAntique;
        int edadAuto = LocalDateTime.now().getYear() - vehiculo.getFabricYear();
        if (edadAuto < 6)
        {
            recargoAntique = 0;
        }
        else if(edadAuto < 11)
        {
            if (Objects.equals(vehiculo.getTipoVehiculo(), "Sedan") || Objects.equals(vehiculo.getTipoVehiculo(), "Hatchback"))
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.05;
            }
            else
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.07;
            }
        }
        else if (edadAuto < 16)
        {
            if (Objects.equals(vehiculo.getTipoVehiculo(), "Sedan") || Objects.equals(vehiculo.getTipoVehiculo(), "Hatchback"))
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.09;
            }
            else
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.11;
            }
        }
        else
        {
            if (Objects.equals(vehiculo.getTipoVehiculo(), "Sedan") || Objects.equals(vehiculo.getTipoVehiculo(), "Hatchback"))
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.15;
            }
            else
            {
                recargoAntique = citaOrigen.getMontoBase() * 0.2;
            }
        }


        //calculo recargo dias de atraso
        double recargoAtraso;
        int diasAtraso = ((citaOrigen.getFechaOut().getYear() - citaOrigen.getFechaReady().getYear()) * 365);
        if (citaOrigen.getFechaOut().getDayOfYear() < citaOrigen.getFechaReady().getDayOfYear())
        {
            diasAtraso = diasAtraso + (citaOrigen.getFechaReady().getDayOfYear() - citaOrigen.getFechaOut().getDayOfYear());
        }
        else
        {
            diasAtraso = diasAtraso + (citaOrigen.getFechaOut().getDayOfYear() - citaOrigen.getFechaReady().getDayOfYear());
        }

        recargoAtraso = citaOrigen.getMontoBase() * (diasAtraso * 0.05);


        //calculo descuento reparaciones
        double descReparaciones = 0;
        int numeroCitasAnteriores = 0;
        List<Cita> historialCitas = citaRepository.getCitasByIdVehiculoOrderByFechaEntradaAsc(citaOrigen.getIdVehiculo());
        LocalDateTime fechaCapaInferior = citaOrigen.getFechaEntrada().minusYears(1);
        int auxReparacionesPorCita;

        for (Cita citaUnitaria : historialCitas){
            if (citaUnitaria.getFechaEntrada().isBefore(citaOrigen.getFechaEntrada()) && citaUnitaria.getFechaEntrada().isAfter(fechaCapaInferior))
            {
                auxReparacionesPorCita = Arrays.asList(citaUnitaria.getNombresReparaciones().split(",")).size();
                numeroCitasAnteriores = numeroCitasAnteriores + auxReparacionesPorCita;
            }
        }

        if (numeroCitasAnteriores < 1){
            descReparaciones = 0;
        } else if (numeroCitasAnteriores < 3) {
            if (Objects.equals(vehiculo.getMotor(), "Gasoline")){
                descReparaciones = citaOrigen.getMontoBase() * 0.05;
            } else if (Objects.equals(vehiculo.getMotor(), "Diesel")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.07;
            } else if (Objects.equals(vehiculo.getMotor(), "Hibrid")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.10;
            } else if (Objects.equals(vehiculo.getMotor(), "Electric")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.08;
            }
        } else if (numeroCitasAnteriores < 6) {
            if (Objects.equals(vehiculo.getMotor(), "Gasoline")){
                descReparaciones = citaOrigen.getMontoBase() * 0.10;
            } else if (Objects.equals(vehiculo.getMotor(), "Diesel")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.12;
            } else if (Objects.equals(vehiculo.getMotor(), "Hibrid")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.15;
            } else if (Objects.equals(vehiculo.getMotor(), "Electric")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.13;
            }
        } else if (numeroCitasAnteriores < 10) {
            if (Objects.equals(vehiculo.getMotor(), "Gasoline")){
                descReparaciones = citaOrigen.getMontoBase() * 0.15;
            } else if (Objects.equals(vehiculo.getMotor(), "Diesel")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.17;
            } else if (Objects.equals(vehiculo.getMotor(), "Hibrid")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.20;
            } else if (Objects.equals(vehiculo.getMotor(), "Electric")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.18;
            }
        } else {
            if (Objects.equals(vehiculo.getMotor(), "Gasoline")){
                descReparaciones = citaOrigen.getMontoBase() * 0.20;
            } else if (Objects.equals(vehiculo.getMotor(), "Diesel")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.22;
            } else if (Objects.equals(vehiculo.getMotor(), "Hibrid")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.25;
            } else if (Objects.equals(vehiculo.getMotor(), "Electric")) {
                descReparaciones = citaOrigen.getMontoBase() * 0.23;
            }
        }


        // verificacion descuento ingreso lunes y jueves
        double descLunJue = 0;
        if (citaOrigen.getFechaEntrada().getHour() > 9 && citaOrigen.getFechaEntrada().getHour() < 12){
            if (citaOrigen.getFechaEntrada().getDayOfWeek() == DayOfWeek.MONDAY || citaOrigen.getFechaEntrada().getDayOfWeek() == DayOfWeek.THURSDAY){
                descLunJue = citaOrigen.getMontoBase() * 0.1;
            }
        }


        //descuento bono built in dentro de clase cita

        // suma total + recargos - descuentos
        double montoRecargos = recargoAtraso + recargoAntique + recargaKilometraje;
        double montoDescuentos = descLunJue + descReparaciones + citaOrigen.getBono();
        double montoTotal = citaOrigen.getMontoBase() + montoRecargos - montoDescuentos;

        // implementacion iva
        montoTotal = montoTotal * 1.19;

        //guardado
        citaOrigen.setMontoFinal((int) montoTotal);
        citaOrigen.setMontoDescuentos(montoDescuentos);
        citaOrigen.setMontoRecargos(montoRecargos);
        return citaRepository.save(citaOrigen);
    }



    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    //Bloque citasUnitariasService

    //para listar todas las citas unitarias, utilidad debug mas que nada
    public List<CitaUnitaria> getCitasUnitarias() {
        return citaUnitariaRepository.findAll();
    }

    public List<CitaUnitaria> obtenerCitasUnitariasNoListas(){
        return citaUnitariaRepository.findCitaUnitariasByFechaReparacionIsNull();
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

        //añadir montoreparacion a total cita padre
        Cita citaPadre = getCitaById(citaUnitaria.getIdCitaPadre());
        citaPadre.setMontoBase(citaPadre.getMontoBase() + citaUnitaria.getMontoReparacion());
        citaRepository.save(citaPadre);

        return citaUnitariaRepository.save(citaUnitaria);
    }

    //borrar una citaUnitaria, borra tambien el string de la reparacion en la cita padre
    @Transactional
    public void borrarCitaUnitariaById(Long id){
        CitaUnitaria citaUnitaria = getCitaUnitariaById(id);
        String reparacion = citaUnitaria.getReparacion();
        Cita citaPadre = getCitaById(citaUnitaria.getIdCitaPadre());
        String reparacionesPadre = citaPadre.getNombresReparaciones();
        List<String> listaReparaciones = Arrays.asList(reparacionesPadre.split(","));

        listaReparaciones.remove(reparacion);

        citaPadre.setNombresReparaciones(String.join(",", listaReparaciones));

        citaRepository.save(citaPadre);
        citaUnitariaRepository.deleteById(id);
    }

    /*@Transactional
    public void borrarCitaUnitariaByIdPadreAndReparacion(Long idPadre, String reparacion){
        CitaUnitaria citaUnitaria = citaUnitariaRepository.getCitaUnitariaByIdCitaPadreAndReparacion(idPadre, reparacion);
        Cita citaPadre = getCitaById(idPadre);

        String reparacionesPadre = citaPadre.getNombresReparaciones();
        List<String> listaReparaciones = Arrays.asList(reparacionesPadre.split(","));

        listaReparaciones.remove(reparacion);

        citaPadre.setNombresReparaciones(String.join(",", listaReparaciones));

        citaRepository.save(citaPadre);
        citaUnitariaRepository.deleteById(citaUnitaria.getId());
    }*/

    @Transactional
    public boolean borrarCitaUnitariaByIdPadreAndReparacion(Long idPadre, String reparacion) {
        CitaUnitaria citaUnitaria = citaUnitariaRepository.getCitaUnitariaByIdCitaPadreAndReparacion(idPadre, reparacion);
        Cita citaPadre = getCitaById(idPadre);

        if (citaUnitaria == null || citaPadre == null) {
            return false;
        }

        String reparacionesPadre = citaPadre.getNombresReparaciones();
        List<String> listaReparaciones = new ArrayList<>(Arrays.asList(reparacionesPadre.split(",")));

        if (!listaReparaciones.remove(reparacion)) {
            return false;
        }

        citaPadre.setNombresReparaciones(String.join(",", listaReparaciones));

        citaRepository.save(citaPadre);
        citaUnitariaRepository.deleteById(citaUnitaria.getId());
        return true;
    }




    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    //Bloque reportes
    @Transactional
    public List<Reporte1Reparacion> generarReporte1(int mes, int year){
        List<Reporte1Reparacion> reporte = generarListaReparacionesReporte1();
        List<CitaUnitaria> citasUnitarias = citaUnitariaRepository.findAll();

        String tipoVehiculoAux;
        String nombreReparacionAux;
        for (CitaUnitaria citaUnitaria : citasUnitarias) {
            if (citaUnitaria.getFechaReparacion().getYear() == year && citaUnitaria.getFechaReparacion().getMonthValue() == mes) {
                tipoVehiculoAux = vehiculosFeignClient.getTipoVehiculoByPatente(citaUnitaria.getPatente());
                nombreReparacionAux = citaUnitaria.getReparacion();
                /*
                 * if reparacion correcta
                 *   if tipovehiculoByPatente == tipo
                 *       reparacion++ */
                for (Reporte1Reparacion reporte1Reparacion : reporte) {
                    if (reporte1Reparacion.getNombreReparacion().equals(nombreReparacionAux)) {
                        if (tipoVehiculoAux.equals("Sedan")){
                            reporte1Reparacion.setCantSedan(1 + reporte1Reparacion.getCantSedan());
                            reporte1Reparacion.setMontoSedan(reporte1Reparacion.getMontoSedan() + citaUnitaria.getMontoReparacion());
                        }
                        else if (tipoVehiculoAux.equals("Hatchback")){
                            reporte1Reparacion.setCantHatchback(1 + reporte1Reparacion.getCantHatchback());
                            reporte1Reparacion.setMontoHatchback(reporte1Reparacion.getMontoHatchback() + citaUnitaria.getMontoReparacion());
                        }
                        else if (tipoVehiculoAux.equals("SUV")){
                            reporte1Reparacion.setCantSUV(1 + reporte1Reparacion.getCantSUV());
                            reporte1Reparacion.setMontoSUV(reporte1Reparacion.getMontoSUV() + citaUnitaria.getMontoReparacion());
                        }
                        else if (tipoVehiculoAux.equals("Pickup")){
                            reporte1Reparacion.setCantPickup(1 + reporte1Reparacion.getCantPickup());
                            reporte1Reparacion.setMontoPickup(reporte1Reparacion.getMontoPickup() + citaUnitaria.getMontoReparacion());
                        }
                        else if (tipoVehiculoAux.equals("Furgoneta")){
                            reporte1Reparacion.setCantFurgoneta(1 + reporte1Reparacion.getCantFurgoneta());
                            reporte1Reparacion.setMontoFurgoneta(reporte1Reparacion.getMontoFurgoneta() + citaUnitaria.getMontoReparacion());
                        }
                        reporte1Reparacion.setCantTotal(1 + reporte1Reparacion.getCantTotal());
                        reporte1Reparacion.setMontoTotal(reporte1Reparacion.getMontoTotal() + citaUnitaria.getMontoReparacion());
                    }
                }

            }
        }
        return reporte;
    }

    public List<Reporte1Reparacion> generarListaReparacionesReporte1(){
        List<Reporte1Reparacion> ListaReparaciones = new ArrayList<>();
        List<String> nombresReparaciones = reparacionesFeignClient.obtenerNombresReparaciones();
        Reporte1Reparacion reparacionAux = new Reporte1Reparacion();
        for(String nombreReparacion : nombresReparaciones){
            reparacionAux.setNombreReparacion(nombreReparacion);
            ListaReparaciones.add(reparacionAux);
        }
        return ListaReparaciones;
    }

    @Transactional
    public List<Reporte2Reparacion> generarReporte2(int mes, int year){
        int mes2;
        int mes3;
        int year2;
        int year3;
        List<Reporte2Reparacion> listaReparaciones = generarListaReparacionesReporte2();
        List<CitaUnitaria> listaCitasUnitarias = citaUnitariaRepository.findAll();

        if (mes == 1){
            mes2 = 12;
            year2 = year - 1;
            mes3 = 11;
            year3 = year - 1;
        }
        else if (mes == 2){
            mes2 = 1;
            year2 = year;
            mes3 = 12;
            year3 = year - 1;
        }
        else {
            mes2 = mes - 1;
            year2 = year;
            mes3 = mes2 - 1;
            year3 = year;
        }

        for (CitaUnitaria citaUnitaria : listaCitasUnitarias) {
            if (citaUnitaria.getFechaReparacion().getYear() == year && citaUnitaria.getFechaReparacion().getMonthValue() == mes){
                for (Reporte2Reparacion reporte2Reparacion : listaReparaciones){
                    if (reporte2Reparacion.getNombreReparacion().equals(citaUnitaria.getReparacion())){
                        reporte2Reparacion.setCantidadReparacion1(reporte2Reparacion.getCantidadReparacion1() + 1);
                        reporte2Reparacion.setMontoReparacion1(reporte2Reparacion.getMontoReparacion1() + citaUnitaria.getMontoReparacion());
                    }
                }
            }
            else if (citaUnitaria.getFechaReparacion().getYear() == year2 && citaUnitaria.getFechaReparacion().getMonthValue() == mes2){
                for (Reporte2Reparacion reporte2Reparacion : listaReparaciones){
                    if (reporte2Reparacion.getNombreReparacion().equals(citaUnitaria.getReparacion())){
                        reporte2Reparacion.setCantidadReparacion2(reporte2Reparacion.getCantidadReparacion2() + 1);
                        reporte2Reparacion.setMontoReparacion2(reporte2Reparacion.getMontoReparacion2() + citaUnitaria.getMontoReparacion());
                    }
                }
            }
            else if (citaUnitaria.getFechaReparacion().getYear() == year3 && citaUnitaria.getFechaReparacion().getMonthValue() == mes3){
                for (Reporte2Reparacion reporte2Reparacion : listaReparaciones){
                    if (reporte2Reparacion.getNombreReparacion().equals(citaUnitaria.getReparacion())){
                        reporte2Reparacion.setCantidadReparacion3(reporte2Reparacion.getCantidadReparacion3() + 1);
                        reporte2Reparacion.setMontoReparacion3(reporte2Reparacion.getMontoReparacion3() + citaUnitaria.getMontoReparacion());
                    }
                }
            }
        }

        for (Reporte2Reparacion reporte2Reparacion : listaReparaciones){
            reporte2Reparacion.setVariacionCantidad2(((reporte2Reparacion.getCantidadReparacion1() / reporte2Reparacion.getCantidadReparacion2()) - 1) * 100);
            reporte2Reparacion.setVariacionMonto2(((reporte2Reparacion.getMontoReparacion1() / reporte2Reparacion.getMontoReparacion2()) - 1) * 100);
            reporte2Reparacion.setVariacionCantidad3(((reporte2Reparacion.getCantidadReparacion2() / reporte2Reparacion.getCantidadReparacion3()) - 1) * 100);
            reporte2Reparacion.setVariacionMonto3(((reporte2Reparacion.getMontoReparacion2() / reporte2Reparacion.getMontoReparacion3()) - 1) * 100);
        }

        return listaReparaciones;
    }

    public List<Reporte2Reparacion> generarListaReparacionesReporte2(){
        List<Reporte2Reparacion> ListaReparaciones = new ArrayList<>();
        List<String> nombresReparaciones = reparacionesFeignClient.obtenerNombresReparaciones();
        Reporte2Reparacion reparacionAux = new Reporte2Reparacion();
        for(String nombreReparacion : nombresReparaciones){
            reparacionAux.setNombreReparacion(nombreReparacion);
            ListaReparaciones.add(reparacionAux);
        }
        return ListaReparaciones;
    }

}
