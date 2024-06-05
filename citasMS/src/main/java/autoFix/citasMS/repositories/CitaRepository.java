package autoFix.citasMS.repositories;

import autoFix.citasMS.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> getCitasByIdVehiculoOrderByFechaInAsc(Long idVehiculo);

    List<Cita> findAllByOrderByFechaInAsc();

    List<Cita> getCitasByFechaReadyIsNullOrderByFechaIn();
}
