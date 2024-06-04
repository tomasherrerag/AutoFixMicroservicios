package autoFix.reparacionesMS.repositories;

import autoFix.reparacionesMS.entities.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {

    Reparacion findByNombre(String nombre);

    void deleteReparacionById(int id);
}