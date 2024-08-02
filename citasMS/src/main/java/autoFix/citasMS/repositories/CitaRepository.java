package autoFix.citasMS.repositories;

import autoFix.citasMS.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> getCitasByIdVehiculoOrderByFechaEntradaAsc(Long idVehiculo);

    List<Cita> findAllByOrderByFechaEntradaAsc();

    List<Cita> getCitasByFechaReadyIsNull();

    List<Cita> getCitasByFechaReadyIsNullOrderByFechaEntradaDesc();

    Cita getCitaByIdVehiculoAndFechaEntrada(Long idVehiculo, LocalDateTime fechaIn);


}
