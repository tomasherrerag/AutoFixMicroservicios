package autoFix.vehiculosMS.Repositories;

import autoFix.vehiculosMS.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    public Vehiculo findVehiculoByPatente(String patente);

    public void deleteVehiculoById(int id);
}
