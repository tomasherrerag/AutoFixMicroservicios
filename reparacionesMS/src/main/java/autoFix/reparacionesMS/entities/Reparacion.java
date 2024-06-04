package autoFix.reparacionesMS.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reparaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Integer id;

    @Column(unique = true, name = "nombre")
    private String nombre;
    @Column(name = "precioGas")
    private int precioGas;
    @Column(name = "precioDiesel")
    private int precioDiesel;
    @Column(name = "precioHibrid")
    private int precioHibrid;
    @Column(name = "precioElectric")
    private int precioElectric;
}
