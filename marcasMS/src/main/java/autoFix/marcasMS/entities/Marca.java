package autoFix.marcasMS.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marca")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Integer id;

    @Column(unique = true,name = "nombre")
    private String nombre;
    @Column(name = "numBono")
    private int numBonos;
    @Column(name = "montoBono")
    private int montoBono;
}
