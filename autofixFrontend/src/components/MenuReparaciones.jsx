import React from 'react';
import { Container, Typography, Button, Box } from '@mui/material';
import { styled } from '@mui/system';

const StyledContainer = styled(Container)(({ theme }) => ({
  backgroundColor: '#ffffff',
  height: '100vh', // Ocupa toda la altura de la ventana
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  boxSizing: 'border-box',
  margin: 0,
  padding: 0,
}));

const Header = styled(Box)(({ theme }) => ({
  position: 'fixed',
  top: 0,
  left: 0,
  width: '100%',
  backgroundColor: '#00b0ff',
  padding: theme.spacing(2),
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  zIndex: 1,
}));

const StyledTitle = styled(Typography)(({ theme }) => ({
  color: '#ffffff',
  textAlign: 'center',
  margin: 0,
}));

const StyledSubtitle = styled(Typography)(({ theme }) => ({
  color: '#ffffff',
  textAlign: 'center',
  margin: 0,
}));

const ButtonContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  width: '100%',
  padding: theme.spacing(2),
  marginTop: theme.spacing(10), // Asegura que los botones no queden ocultos bajo el header
}));

const StyledButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#00b0ff',
  color: '#ffffff',
  marginBottom: theme.spacing(4), // Espacio vertical entre los botones
  width: '100%', // Botones ocupan toda la anchura del contenedor
  maxWidth: '500px', // Limita la anchura máxima de los botones
  padding: theme.spacing(2), // Mantiene el tamaño del botón
  fontSize: '1rem', // Mantiene el tamaño de la fuente
  '&:hover': {
    backgroundColor: '#0091ea',
  },
}));

const MenuReparaciones = () => {
  return (
    <StyledContainer>
      <Header>
        <StyledTitle variant="h2">AutoFix Calculator</StyledTitle>
        <StyledSubtitle variant="h6">home &gt; menú reparaciones</StyledSubtitle>
      </Header>
      <ButtonContainer>
        <StyledButton variant="contained">Agregar Reparación</StyledButton>
        <StyledButton variant="contained">Editar Reparaciones</StyledButton>
      </ButtonContainer>
    </StyledContainer>
  );
};

export default MenuReparaciones;
