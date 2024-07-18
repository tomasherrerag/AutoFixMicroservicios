import React from 'react';
import { Container, Typography, Button, Box } from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';

const StyledContainer = styled(Container)(({ theme }) => ({
  backgroundColor: '#ffffff',
  height: '100vh',
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
  marginTop: theme.spacing(10),
}));

const StyledButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#00b0ff',
  color: '#ffffff',
  marginBottom: theme.spacing(4),
  width: '100%',
  maxWidth: '500px',
  padding: theme.spacing(2),
  fontSize: '1rem',
  '&:hover': {
    backgroundColor: '#0091ea',
  },
}));

const BackButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#ff1744',
  color: '#ffffff',
  position: 'absolute',
  top: theme.spacing(2),
  left: theme.spacing(2),
  padding: theme.spacing(1),
  zIndex: 2,
  '&:hover': {
    backgroundColor: '#d50000',
  },
}));

const MenuReparaciones = () => {
  const navigate = useNavigate();

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/')}>
          Volver
        </BackButton>
        <StyledTitle variant="h2">AutoFix Calculator</StyledTitle>
        <StyledSubtitle variant="h6">home &gt; menú reparaciones</StyledSubtitle>
      </Header>
      <ButtonContainer>
        <StyledButton variant="contained" onClick={() => navigate('/agregar-reparacion')}>
          Agregar Reparación
        </StyledButton>
        <StyledButton variant="contained" onClick={() => navigate('/listar-reparaciones')}>
          Editar Reparaciones
        </StyledButton>
      </ButtonContainer>
    </StyledContainer>
  );
};

export default MenuReparaciones;
