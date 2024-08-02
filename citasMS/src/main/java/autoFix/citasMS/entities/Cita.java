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
    private LocalDateTime fechaEntrada;

    @Column(name = "montoBase")
    private int montoBase;
    @Column(name = "montoRecargos")
    private double montoRecargos;
    @Column(name = "montoDescuentos")
    private double montoDescuentos;
    @Column(name = "montoIVA")
    private double montoIVA;

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
    @Column(name = "nombresReparaciones")
    private String nombresReparaciones;
}