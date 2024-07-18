import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, TextField, Button, CircularProgress } from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate, useParams } from 'react-router-dom';

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

const ModifyReparacion = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [reparacion, setReparacion] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get(`http://localhost:8080/autofix/reparaciones/${id}`)
      .then(response => {
        setReparacion(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, [id]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setReparacion({
      ...reparacion,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.put(`http://localhost:8080/autofix/reparaciones/${id}`, reparacion)
      .then(response => {
        console.log('Reparación actualizada:', response.data);
        navigate('/listar-reparaciones');
      })
      .catch(error => {
        console.error('Error updating reparacion:', error);
      });
  };

  if (loading) {
    return <CircularProgress />;
  }

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/listar-reparaciones')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reparaciones &gt; listar reparaciones &gt; modificar reparación</StyledSubtitle>
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
          <Button variant="contained" color="primary" type="submit">
            Guardar
          </Button>
          <Button variant="contained" color="secondary" onClick={() => navigate('/listar-reparaciones')}>
            Volver
          </Button>
        </Box>
      </Box>
    </StyledContainer>
  );
};

export default ModifyReparacion;
