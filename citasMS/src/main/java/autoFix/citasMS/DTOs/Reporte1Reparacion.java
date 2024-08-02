package autoFix.citasMS.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Reporte1Reparacion {
    String nombreReparacion;
    int cantSedan;
    int cantHatchback;
    int cantSUV;
    int cantPickup;
    int cantFurgoneta;
    int montoSedan;
    int montoHatchback;
    int montoSUV;
    int montoPickup;
    int montoFurgoneta;

    int cantTotal;
    int montoTotal;
}
