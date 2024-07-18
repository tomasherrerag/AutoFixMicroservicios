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

const ModifyButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#00b0ff',
  color: '#ffffff',
  marginRight: theme.spacing(1),
  '&:hover': {
    backgroundColor: '#0091ea',
  },
}));

const DeleteButton = styled(Button)(({ theme }) => ({
  backgroundColor: '#ff1744',
  color: '#ffffff',
  '&:hover': {
    backgroundColor: '#d50000',
  },
}));

const ListarVehiculos = () => {
  const navigate = useNavigate();
  const [vehiculos, setVehiculos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selectedVehiculo, setSelectedVehiculo] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/vehiculos/')
      .then(response => {
        setVehiculos(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleModify = (id) => {
    navigate(`/modificar-vehiculo/${id}`);
  };

  const handleDelete = (id) => {
    setSelectedVehiculo(vehiculos.find(vehiculo => vehiculo.id === id));
    setOpen(true);
  };

  const handleConfirmDelete = () => {
    axios.delete(`http://localhost:8080/autofix/vehiculos/${selectedVehiculo.id}`)
      .then(response => {
        console.log(response.data);
        setVehiculos(vehiculos.filter(vehiculo => vehiculo.id !== selectedVehiculo.id));
        setOpen(false);
        setSelectedVehiculo(null);
      })
      .catch(error => {
        console.error('Error deleting vehiculo:', error);
      });
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedVehiculo(null);
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-vehiculos')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú vehículos &gt; listar vehículos</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Patente</TableCell>
                <TableCell>Modelo</TableCell>
                <TableCell>Año de Fabricación</TableCell>
                <TableCell>Asientos</TableCell>
                <TableCell>Motor</TableCell>
                <TableCell>Tipo de Vehículo</TableCell>
                <TableCell>ID Marca</TableCell>
                <TableCell>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {vehiculos.map(vehiculo => (
                <TableRow key={vehiculo.id}>
                  <TableCell>{vehiculo.id}</TableCell>
                  <TableCell>{vehiculo.patente}</TableCell>
                  <TableCell>{vehiculo.modelo}</TableCell>
                  <TableCell>{vehiculo.fabricYear}</TableCell>
                  <TableCell>{vehiculo.asientos}</TableCell>
                  <TableCell>{vehiculo.motor}</TableCell>
                  <TableCell>{vehiculo.tipoVehiculo}</TableCell>
                  <TableCell>{vehiculo.idMarca}</TableCell>
                  <TableCell>
                    <Box display="flex" justifyContent="flex-start">
                      <ModifyButton variant="contained" onClick={() => handleModify(vehiculo.id)}>
                        Modificar
                      </ModifyButton>
                      <DeleteButton variant="contained" onClick={() => handleDelete(vehiculo.id)}>
                        Borrar
                      </DeleteButton>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Confirmar Borrado"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {`¿Está seguro que quiere borrar ${selectedVehiculo?.modelo}?`}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            No
          </Button>
          <Button onClick={handleConfirmDelete} color="primary" autoFocus>
            Sí
          </Button>
        </DialogActions>
      </Dialog>
    </StyledContainer>
  );
};

export default ListarVehiculos;
