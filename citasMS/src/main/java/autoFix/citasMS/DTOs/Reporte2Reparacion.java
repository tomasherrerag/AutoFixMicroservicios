package autoFix.citasMS.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Reporte2Reparacion {
    String nombreReparacion;
    int cantidadReparacion1;
    int cantidadReparacion2;
    int cantidadReparacion3;
    int montoReparacion1;
    int montoReparacion2;
    int montoReparacion3;
    int variacionCantidad2;
    int variacionMonto2;
    int variacionCantidad3;
    int variacionMonto3;
}
