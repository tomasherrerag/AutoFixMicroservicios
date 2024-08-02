package autoFix.citasMS.DTOs;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehiculo {

    private Long id;

    private String patente;
    private String modelo;
    private int fabricYear;
    private int asientos;
    private String motor;
    private String tipoVehiculo;
    private int idMarca;
}
