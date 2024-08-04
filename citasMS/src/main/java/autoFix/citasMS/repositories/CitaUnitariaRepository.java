package autoFix.citasMS.repositories;

import autoFix.citasMS.entities.CitaUnitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaUnitariaRepository extends JpaRepository<CitaUnitaria, Long> {

    List<CitaUnitaria> findCitaUnitariasByIdCitaPadre(long idCitaPadre);

    List<CitaUnitaria> findCitaUnitariasByPatente(String patente);

    boolean existsCitaUnitariaByIdCitaPadreAndFechaReparacionIsNull(long idCitaPadre);

    CitaUnitaria getCitaUnitariaByIdCitaPadreAndReparacion(long idCitaPadre, String reparacion);

    List<CitaUnitaria> findCitaUnitariasByFechaReparacionIsNull();

    List<CitaUnitaria> findCitaUnitariasByFechaReparacionIsNotNull();

}
