import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, TextField, Button, CircularProgress, MenuItem, Select, FormControl, InputLabel, Checkbox, ListItemText } from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';

const StyledContainer = styled(Container)(({ theme }) => ({
  backgroundColor: '#ffffff',
  height: '100vh',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'flex-start',
  boxSizing: 'border-box',
  margin: 0,
  paddingTop: theme.spacing(8),
  overflowY: 'auto',
}));

const Header = styled(Box)(({ theme }) => ({
  position: 'fixed',
  top: 0,
  left: 0,
  width: '100%',
  backgroundColor: '#00b0ff',
  padding: theme.spacing(1),
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  zIndex: 1,
  boxSizing: 'border-box',
}));

const StyledTitle = styled(Typography)(({ theme }) => ({
  color: '#ffffff',
  margin: 0,
  fontSize: '1.5rem',
  marginRight: theme.spacing(2),
}));

const StyledSubtitle = styled(Typography)(({ theme }) => ({
  color: '#ffffff',
  margin: 0,
  fontSize: '1rem',
  flexGrow: 1,
  textAlign: 'center',
}));

const BackButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#ff1744',
  color: '#ffffff',
  padding: theme.spacing(0.5),
  '&:hover': {
    backgroundColor: '#d50000',
  },
  marginLeft: theme.spacing(1),
}));

const AgregarCita = () => {
  const navigate = useNavigate();
  const [cita, setCita] = useState({
    patente: '',
    kilometraje: '',
    bono: '',
    nombresReparaciones: []
  });
  const [vehiculo, setVehiculo] = useState(null);
  const [reparaciones, setReparaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/reparaciones/obtenerNombresReparaciones')
      .then(response => {
        setReparaciones(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching reparaciones:', error);
        setLoading(false);
      });
  }, []);

  useEffect(() => {
    validateForm();
  }, [cita]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCita({
      ...cita,
      [name]: value,
    });

    if (name === 'patente') {
      axios.get(`http://localhost:8080/autofix/vehiculos/getIdVehiculoByPatente?patente=${value}`)
        .then(response => {
          const vehiculoId = response.data;
          if (vehiculoId) {
            return axios.get(`http://localhost:8080/autofix/vehiculos/${vehiculoId}`);
          }
          return Promise.reject('Vehículo no encontrado');
        })
        .then(response => {
          setVehiculo(response.data);
        })
        .catch(error => {
          console.error('Error fetching vehiculo:', error);
          setVehiculo(null);
        });
    }
  };

  const handleReparacionesChange = (event) => {
    const { value } = event.target;
    setCita({
      ...cita,
      nombresReparaciones: typeof value === 'string' ? value.split(',') : value,
    });
  };

  const validateForm = () => {
    const { patente, kilometraje, nombresReparaciones, bono } = cita;
    const isPatenteValid = vehiculo !== null;
    const isKilometrajeValid = !isNaN(parseInt(kilometraje, 10)) && kilometraje.trim() !== '';
    const isReparacionesSelected = nombresReparaciones.length > 0;
    const isBonoSelected = bono !== '';

    setIsButtonDisabled(!(isPatenteValid && isKilometrajeValid && isReparacionesSelected && isBonoSelected));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const nuevaCitaDto = {
      patente: cita.patente,
      listaReparaciones: cita.nombresReparaciones,
      kilometraje: parseInt(cita.kilometraje, 10),
      bono: parseInt(cita.bono, 10)
    };

    axios.post('http://localhost:8080/autofix/citas/in/', nuevaCitaDto)
      .then(response => {
        console.log('Cita agregada:', response.data);
        navigate('/eliminar-citas');
      })
      .catch(error => {
        console.error('Error adding cita:', error);
      });
  };

  if (loading) {
    return <CircularProgress />;
  }

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-citas')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú citas &gt; agregar cita</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3, width: '100%', maxWidth: 600 }}>
        <TextField
          fullWidth
          label="Patente"
          name="patente"
          value={cita.patente}
          onChange={handleInputChange}
          margin="normal"
        />
        {vehiculo && (
          <>
            <Typography>Modelo: {vehiculo.modelo}</Typography>
            <Typography>Año de Fabricación: {vehiculo.fabricYear}</Typography>
          </>
        )}
        <TextField
          fullWidth
          label="Kilometraje"
          name="kilometraje"
          type="number"
          value={cita.kilometraje}
          onChange={handleInputChange}
          margin="normal"
        />
        <FormControl fullWidth margin="normal">
          <InputLabel id="reparaciones-label">Reparaciones</InputLabel>
          <Select
            labelId="reparaciones-label"
            name="nombresReparaciones"
            multiple
            value={cita.nombresReparaciones}
            onChange={handleReparacionesChange}
            renderValue={(selected) => selected.join(', ')}
          >
            {reparaciones.map((reparacion) => (
              <MenuItem
                key={reparacion}
                value={reparacion}
                style={{
                  backgroundColor: cita.nombresReparaciones.includes(reparacion) ? '#bbdefb' : 'inherit',
                  color: cita.nombresReparaciones.includes(reparacion) ? '#0d47a1' : 'inherit'
                }}
              >
                <Checkbox checked={cita.nombresReparaciones.includes(reparacion)} />
                <ListItemText primary={reparacion} />
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl fullWidth margin="normal">
          <InputLabel id="bono-label">Aplicación de Bono</InputLabel>
          <Select
            labelId="bono-label"
            name="bono"
            value={cita.bono}
            onChange={handleInputChange}
            label="Aplicación de Bono"
          >
            <MenuItem value={0}>No</MenuItem>
            <MenuItem value={1}>Sí</MenuItem>
          </Select>
        </FormControl>
        <Box display="flex" justifyContent="space-between" mt={2}>
          <Button variant="contained" color="primary" type="submit" disabled={isButtonDisabled}>
            Guardar
          </Button>
          <Button variant="contained" color="secondary" onClick={() => navigate('/menu-citas')}>
            Volver
          </Button>
        </Box>
      </Box>
    </StyledContainer>
  );
};

export default AgregarCita;
