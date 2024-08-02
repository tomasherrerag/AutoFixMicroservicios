import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
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

const CloseAppointmentButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#4caf50', // Color verde
  color: '#ffffff',
  '&:hover': {
    backgroundColor: '#388e3c', // Verde más oscuro al pasar el cursor
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

const CerrarCita = () => {
  const navigate = useNavigate();
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selectedCita, setSelectedCita] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/citas/Cerradas')
      .then(response => {
        setCitas(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleCloseCita = (id) => {
    setSelectedCita(citas.find(cita => cita.id === id));
    setOpen(true);
  };

  const handleConfirmCloseCita = () => {
    axios.put(`http://localhost:8080/autofix/citas/ready/${selectedCita.id}`)
      .then(response => {
        const updatedCitas = citas.map(cita => {
          if (cita.id === selectedCita.id) {
            return response.data;
          }
          return cita;
        });
        setCitas(updatedCitas);
        setOpen(false);
        setSelectedCita(null);
      })
      .catch(error => {
        console.error('Error closing cita:', error);
      });
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedCita(null);
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-citas')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú citas &gt; cerrar citas</StyledSubtitle>
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
                <StyledTableCell>ID Vehículo</StyledTableCell>
                <StyledTableCell>Reparaciones</StyledTableCell>
                <StyledTableCell>Monto Base</StyledTableCell>
                <StyledTableCell>Monto Final</StyledTableCell>
                <StyledTableCell>Fecha de Entrada</StyledTableCell>
                <StyledTableCell>Fecha de Listo</StyledTableCell>
                <StyledTableCell>Fecha de Salida</StyledTableCell>
                <StyledTableCell>Monto Recargos</StyledTableCell>
                <StyledTableCell>Monto Descuentos</StyledTableCell>
                <StyledTableCell>Bono</StyledTableCell>
                <StyledTableCell>Kilometraje</StyledTableCell>
                <StyledTableCell>Monto IVA</StyledTableCell>
                <StyledTableCell>Acciones</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {citas.map(cita => (
                <TableRow key={cita.id}>
                  <StyledTableCell>{cita.id}</StyledTableCell>
                  <StyledTableCell>{cita.idVehiculo}</StyledTableCell>
                  <StyledTableCell>
                    {cita.nombresReparaciones.split(',').map((reparacion, index) => (
                      <div key={index}>{reparacion}</div>
                    ))}
                  </StyledTableCell>
                  <StyledTableCell>{cita.montoBase}</StyledTableCell>
                  <StyledTableCell>{cita.montoFinal}</StyledTableCell>
                  <StyledTableCell>{new Date(cita.fechaEntrada).toLocaleString()}</StyledTableCell>
                  <StyledTableCell>{cita.fechaReady ? new Date(cita.fechaReady).toLocaleString() : 'N/A'}</StyledTableCell>
                  <StyledTableCell>{cita.fechaOut ? new Date(cita.fechaOut).toLocaleString() : 'N/A'}</StyledTableCell>
                  <StyledTableCell>{cita.montoRecargos}</StyledTableCell>
                  <StyledTableCell>{cita.montoDescuentos}</StyledTableCell>
                  <StyledTableCell>{cita.bono}</StyledTableCell>
                  <StyledTableCell>{cita.kilometraje}</StyledTableCell>
                  <StyledTableCell>{cita.montoIVA}</StyledTableCell>
                  <StyledTableCell>
                    <Box display="flex" justifyContent="flex-start">
                      <CloseAppointmentButton variant="contained" onClick={() => handleCloseCita(cita.id)}>
                        Cerrar Cita
                      </CloseAppointmentButton>
                    </Box>
                  </StyledTableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </StyledTableContainer>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Cerrar Cita</DialogTitle>
        <DialogContent>
          <DialogContentText>
            ¿Está seguro que quiere cerrar la cita seleccionada?
          </DialogContentText>
          {selectedCita && (
            <Box>
              <Typography>ID Vehículo: {selectedCita.idVehiculo}</Typography>
              <Typography>Monto Base: {selectedCita.montoBase}</Typography>
              <Typography>Reparaciones: {selectedCita.nombresReparaciones}</Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancelar
          </Button>
          <Button onClick={handleConfirmCloseCita} color="secondary">
            Confirmar
          </Button>
        </DialogActions>
      </Dialog>
    </StyledContainer>
  );
};

export default CerrarCita;
