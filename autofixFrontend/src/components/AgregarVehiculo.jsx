import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, TextField, Button, CircularProgress, MenuItem, Select, FormControl, InputLabel } from '@mui/material';
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

const AgregarVehiculo = () => {
  const navigate = useNavigate();
  const [vehiculo, setVehiculo] = useState({
    patente: '',
    modelo: '',
    fabricYear: '',
    asientos: '',
    motor: '',
    tipoVehiculo: '',
    idMarca: ''
  });
  const [marcas, setMarcas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/marcas/')
      .then(response => {
        setMarcas(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching marcas:', error);
        setLoading(false);
      });
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setVehiculo({
      ...vehiculo,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post('http://localhost:8080/autofix/vehiculos/', vehiculo)
      .then(response => {
        console.log('Vehículo agregado:', response.data);
        navigate('/listar-vehiculos');
      })
      .catch(error => {
        console.error('Error adding vehiculo:', error);
      });
  };

  if (loading) {
    return <CircularProgress />;
  }

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-vehiculos')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú vehículos &gt; agregar vehículo</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3, width: '100%', maxWidth: 600 }}>
        <TextField
          fullWidth
          label="Patente"
          name="patente"
          value={vehiculo.patente}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Modelo"
          name="modelo"
          value={vehiculo.modelo}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Año de Fabricación"
          name="fabricYear"
          type="number"
          value={vehiculo.fabricYear}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Asientos"
          name="asientos"
          type="number"
          value={vehiculo.asientos}
          onChange={handleInputChange}
          margin="normal"
        />
        <FormControl fullWidth margin="normal">
          <InputLabel id="motor-label">Motor</InputLabel>
          <Select
            labelId="motor-label"
            name="motor"
            value={vehiculo.motor}
            onChange={handleInputChange}
            label="Motor"
          >
            <MenuItem value="Gasoline">Gasoline</MenuItem>
            <MenuItem value="Diesel">Diesel</MenuItem>
            <MenuItem value="Electric">Electric</MenuItem>
            <MenuItem value="Hibrid">Hibrid</MenuItem>
          </Select>
        </FormControl>
        <FormControl fullWidth margin="normal">
          <InputLabel id="tipoVehiculo-label">Tipo de Vehículo</InputLabel>
          <Select
            labelId="tipoVehiculo-label"
            name="tipoVehiculo"
            value={vehiculo.tipoVehiculo}
            onChange={handleInputChange}
            label="Tipo de Vehículo"
          >
            <MenuItem value="Sedan">Sedan</MenuItem>
            <MenuItem value="Hatchback">Hatchback</MenuItem>
            <MenuItem value="SUV">SUV</MenuItem>
            <MenuItem value="Pickup">Pickup</MenuItem>
            <MenuItem value="Furgoneta">Furgoneta</MenuItem>
          </Select>
        </FormControl>
        <TextField
          select
          fullWidth
          label="Marca"
          name="idMarca"
          value={vehiculo.idMarca}
          onChange={handleInputChange}
          margin="normal"
        >
          {marcas.map(marca => (
            <MenuItem key={marca.id} value={marca.id}>
              {marca.nombre}
            </MenuItem>
          ))}
        </TextField>
        <Box display="flex" justifyContent="space-between" mt={2}>
          <Button variant="contained" color="primary" type="submit">
            Guardar
          </Button>
          <Button variant="contained" color="secondary" onClick={() => navigate('/menu-vehiculos')}>
            Volver
          </Button>
        </Box>
      </Box>
    </StyledContainer>
  );
};

export default AgregarVehiculo;
