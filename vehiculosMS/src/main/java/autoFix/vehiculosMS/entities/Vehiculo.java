package autoFix.vehiculosMS.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(unique = true, name = "patente")
    private String patente;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "fabricYear")
    private int fabricYear;
    @Column(name = "asientos")
    private int asientos;
    @Column(name = "motor")
    private String motor;
    @Column(name = "tipoVehiculo")
    private String tipoVehiculo;
    @Column(name = "idMarca")
    private int idMarca;
}
