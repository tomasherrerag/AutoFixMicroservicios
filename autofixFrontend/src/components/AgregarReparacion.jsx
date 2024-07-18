import React, { useState } from 'react';
import axios from 'axios';
import { Container, Typography, Box, TextField, Button, CircularProgress } from '@mui/material';
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

const SuccessOverlay = styled(Box)(({ theme }) => ({
  position: 'fixed',
  top: 0,
  left: 0,
  width: '100%',
  height: '100%',
  backgroundColor: 'rgba(0, 0, 0, 0.8)',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  color: '#ffffff',
  zIndex: 10,
}));

const AgregarReparacion = () => {
  const navigate = useNavigate();
  const [reparacion, setReparacion] = useState({
    nombre: '',
    precioGas: '',
    precioDiesel: '',
    precioHibrid: '',
    precioElectric: '',
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setReparacion({
      ...reparacion,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/autofix/reparaciones/', reparacion);
      setSuccess(true);
      setTimeout(() => {
        navigate('/menu-reparaciones');
      }, 3000);
    } catch (error) {
      console.error('Error al agregar la reparación:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-reparaciones')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reparaciones &gt; agregar reparación</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3, width: '100%', maxWidth: 600 }}>
        <TextField
          fullWidth
          label="Nombre"
          name="nombre"
          value={reparacion.nombre}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Precio Gas"
          name="precioGas"
          type="number"
          value={reparacion.precioGas}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Precio Diesel"
          name="precioDiesel"
          type="number"
          value={reparacion.precioDiesel}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Precio Híbrido"
          name="precioHibrid"
          type="number"
          value={reparacion.precioHibrid}
          onChange={handleInputChange}
          margin="normal"
        />
        <TextField
          fullWidth
          label="Precio Eléctrico"
          name="precioElectric"
          type="number"
          value={reparacion.precioElectric}
          onChange={handleInputChange}
          margin="normal"
        />
        <Box display="flex" justifyContent="space-between" mt={2}>
          <Button variant="contained" color="primary" type="submit" disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Guardar'}
          </Button>
          <Button variant="contained" color="secondary" onClick={() => navigate('/menu-reparaciones')}>
            Volver
          </Button>
        </Box>
      </Box>
      {success && (
        <SuccessOverlay>
          <Typography variant="h4">Reparación agregada exitosamente</Typography>
          <Typography variant="h6">Será redireccionado al menú de reparaciones en breve</Typography>
        </SuccessOverlay>
      )}
    </StyledContainer>
  );
};

export default AgregarReparacion;
