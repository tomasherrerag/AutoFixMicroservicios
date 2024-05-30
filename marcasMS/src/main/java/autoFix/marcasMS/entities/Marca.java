package autoFix.marcasMS.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.scheduling.support.SimpleTriggerContext;

@Entity
@Table(name = "marca")
@Getter
@Setter
@NoArgsConstructor
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Integer id;

    @Column(unique = true,name = "nombre")
    private String nombre;
    @Column(name = "numBono")
    private int numBono;
    @Column(name = "montoBono")
    private int montoBono;
}
