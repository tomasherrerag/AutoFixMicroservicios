package autoFix.citasMS.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citasUnitarias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitaUnitaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "patente", nullable = false)
    private String patente;
    @Column(name = "reparacion", nullable = false)
    private String reparacion;
    @Column(name = "fechaReady")
    private LocalDateTime fechaReparacion;
    @Column(name = "montoReparacion")
    private int montoReparacion;

    @Column(name = "citaPadre")
    private Long idCitaPadre;
}
