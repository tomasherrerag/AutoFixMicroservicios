package autoFix.citasMS.repositories;

import autoFix.citasMS.entities.CitaUnitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaUnitariaRepository extends JpaRepository<CitaUnitaria, Long> {
}
