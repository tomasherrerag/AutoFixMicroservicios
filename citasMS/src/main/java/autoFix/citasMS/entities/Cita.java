package autoFix.citasMS.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "fechaIn")
    private LocalDateTime fechaIn;
    @Column(name = "montoFinal")
    private int montoFinal;
    @Column(name = "fechaReady")
    private LocalDateTime fechaReady;
    @Column(name = "fechaOut")
    private LocalDateTime fechaOut;
    @Column(name = "bono")
    private int bono;
    @Column(name = "kilometraje")
    private int kilometraje;
    @Column(name = "idVehiculo")

    private Long idVehiculo;
    private String nombresReparaciones;
}