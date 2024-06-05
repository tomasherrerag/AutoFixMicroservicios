package autoFix.marcasMS.repositories;

import autoFix.marcasMS.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    List<Marca> findAllByOrderByNombreAsc();

    Marca findByNombre(String nombre);

    void deleteMarcaById(int id);
}
