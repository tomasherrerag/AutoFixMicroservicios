import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Typography, Box, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Select, MenuItem, InputLabel, FormControl } from '@mui/material';
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

const DeleteRepairsButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#9c27b0',
  color: '#ffffff',
  marginRight: theme.spacing(1),
  '&:hover': {
    backgroundColor: '#7b1fa2',
  },
}));

const DeleteAppointmentButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#ff1744',
  color: '#ffffff',
  '&:hover': {
    backgroundColor: '#d50000',
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

const EliminarCitas = () => {
  const navigate = useNavigate();
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selectedCita, setSelectedCita] = useState(null);
  const [openRepairDialog, setOpenRepairDialog] = useState(false);
  const [selectedRepair, setSelectedRepair] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/citas/')
      .then(response => {
        setCitas(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleDeleteRepairs = (cita) => {
    setSelectedCita(cita);
    setOpenRepairDialog(true);
  };

  const handleConfirmDeleteRepair = () => {
    axios.delete(`http://localhost:8080/autofix/citas/Unitarias/deleteByIdPadreAndReparacion`, {
      params: {
        idPadre: selectedCita.id,
        reparacion: selectedRepair
      }
    })
      .then(response => {
        const updatedCitas = citas.map(cita => {
          if (cita.id === selectedCita.id) {
            cita.nombresReparaciones = cita.nombresReparaciones.split(',').filter(rep => rep !== selectedRepair).join(',');
          }
          return cita;
        });
        setCitas(updatedCitas);
        setOpenRepairDialog(false);
        setSelectedRepair('');
      })
      .catch(error => {
        console.error('Error deleting repair:', error);
      });
  };

  const handleModify = (id) => {
    navigate(`/modificar-cita/${id}`);
  };

  const handleDelete = (id) => {
    setSelectedCita(citas.find(cita => cita.id === id));
    setOpen(true);
  };

  const handleConfirmDelete = () => {
    axios.delete(`http://localhost:8080/autofix/citas/delete/${selectedCita.id}`)
      .then(response => {
        setCitas(citas.filter(cita => cita.id !== selectedCita.id));
        setOpen(false);
        setSelectedCita(null);
      })
      .catch(error => {
        console.error('Error deleting cita:', error);
      });
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedCita(null);
  };

  const handleCloseRepairDialog = () => {
    setOpenRepairDialog(false);
    setSelectedRepair('');
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-citas')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú citas &gt; eliminar citas/reparaciones</StyledSubtitle>
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
                      <DeleteRepairsButton variant="contained" onClick={() => handleDeleteRepairs(cita)}>
                        Eliminar Reparaciones
                      </DeleteRepairsButton>
                      <DeleteAppointmentButton variant="contained" onClick={() => handleDelete(cita.id)}>
                        Borrar Cita
                      </DeleteAppointmentButton>
                    </Box>
                  </StyledTableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </StyledTableContainer>
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Eliminar Cita</DialogTitle>
        <DialogContent>
          <DialogContentText>
            ¿Está seguro que quiere borrar la cita seleccionada?
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
          <Button onClick={handleConfirmDelete} color="secondary">
            Eliminar
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={openRepairDialog} onClose={handleCloseRepairDialog}>
        <DialogTitle>Eliminar Reparación</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Selecciona la reparación a eliminar
          </DialogContentText>
          <FormControl fullWidth>
            <InputLabel>Reparación</InputLabel>
            <Select
              value={selectedRepair}
              onChange={(e) => setSelectedRepair(e.target.value)}
            >
              {selectedCita && selectedCita.nombresReparaciones.split(',').map((reparacion, index) => (
                <MenuItem key={index} value={reparacion}>{reparacion}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseRepairDialog} color="primary">
            Cancelar
          </Button>
          <Button onClick={handleConfirmDeleteRepair} color="secondary">
            Eliminar
          </Button>
        </DialogActions>
      </Dialog>
    </StyledContainer>
  );
};

export default EliminarCitas;