package autoFix.marcasMS.repositories;

import autoFix.marcasMS.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    public List<Marca> findAllByOrderByNombreAsc();

    public Marca findByNombre(String nombre);

    public void deleteMarcaById(int id);
}
