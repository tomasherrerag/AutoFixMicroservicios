package autoFix.citasMS.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NuevaCitaUnitariaDTO {
    private String patente;
    private String reparacion;
    private Long idPadre;
}
