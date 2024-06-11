package autoFix.citasMS.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NuevaCitaDTO {
    private String patente;
    private List<String> listaReparaciones;
    private int kilometraje;
    private int bono;
}