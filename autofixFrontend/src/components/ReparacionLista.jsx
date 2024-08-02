import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
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
  width: '100%',
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

const ReadyButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#4caf50',
  color: '#ffffff',
  '&:hover': {
    backgroundColor: '#388e3c',
  },
}));

const StyledTableContainer = styled(TableContainer)(({ theme }) => ({
  width: '100%',
  marginTop: theme.spacing(2),
}));

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  padding: theme.spacing(1),
  whiteSpace: 'nowrap',
}));

const ReparacionLista = () => {
  const navigate = useNavigate();
  const [citasUnitarias, setCitasUnitarias] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/citas/Unitarias/NoListas')
      .then(response => {
        setCitasUnitarias(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleMarkReady = (id) => {
    axios.put(`http://localhost:8080/autofix/citas/Unitarias/ready/${id}`)
      .then(response => {
        // Refrescar la página después de marcar la cita como lista
        window.location.reload();
      })
      .catch(error => {
        console.error('Error marking cita as ready:', error);
      });
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-citas')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reparaciones &gt; reparación lista</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      {loading ? (
        <CircularProgress />
      ) : (
        <StyledTableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <StyledTableCell>ID</StyledTableCell>
                <StyledTableCell>Patente</StyledTableCell>
                <StyledTableCell>Reparación</StyledTableCell>
                <StyledTableCell>Fecha de Reparación</StyledTableCell>
                <StyledTableCell>Monto Reparación</StyledTableCell>
                <StyledTableCell>Acciones</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {citasUnitarias.map(citaUnitaria => (
                <TableRow key={citaUnitaria.id}>
                  <StyledTableCell>{citaUnitaria.id}</StyledTableCell>
                  <StyledTableCell>{citaUnitaria.patente}</StyledTableCell>
                  <StyledTableCell>{citaUnitaria.reparacion}</StyledTableCell>
                  <StyledTableCell>{citaUnitaria.fechaReparacion ? new Date(citaUnitaria.fechaReparacion).toLocaleString() : 'N/A'}</StyledTableCell>
                  <StyledTableCell>{citaUnitaria.montoReparacion}</StyledTableCell>
                  <StyledTableCell>
                    <Box display="flex" justifyContent="flex-start">
                      <ReadyButton variant="contained" onClick={() => handleMarkReady(citaUnitaria.id)}>
                        Marcar Listo
                      </ReadyButton>
                    </Box>
                  </StyledTableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </StyledTableContainer>
      )}
    </StyledContainer>
  );
};

export default ReparacionLista;
