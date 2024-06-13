package autoFix.citasMS.repositories;

import autoFix.citasMS.entities.CitaUnitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaUnitariaRepository extends JpaRepository<CitaUnitaria, Long> {

    List<CitaUnitaria> findCitaUnitariasByIdCitaPadre(long idCitaPadre);

    List<CitaUnitaria> findCitaUnitariasByFechaReparacionEmpty();

    List<CitaUnitaria> findCitaUnitariasByPatente(String patente);
}
