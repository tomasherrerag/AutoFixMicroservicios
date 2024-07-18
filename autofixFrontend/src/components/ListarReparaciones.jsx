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

const ListarReparaciones = () => {
  const navigate = useNavigate();
  const [reparaciones, setReparaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selectedReparacion, setSelectedReparacion] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/reparaciones/')
      .then(response => {
        setReparaciones(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleModify = (id) => {
    navigate(`/modificar-reparacion/${id}`);
  };

  const handleDelete = (id) => {
    setSelectedReparacion(reparaciones.find(reparacion => reparacion.id === id));
    setOpen(true);
  };

  const handleConfirmDelete = () => {
    axios.delete(`http://localhost:8080/autofix/reparaciones/${selectedReparacion.id}`)
      .then(response => {
        console.log(response.data);
        setReparaciones(reparaciones.filter(reparacion => reparacion.id !== selectedReparacion.id));
        setOpen(false);
        setSelectedReparacion(null);
      })
      .catch(error => {
        console.error('Error deleting reparacion:', error);
      });
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedReparacion(null);
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-reparaciones')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reparaciones &gt; listar reparaciones</StyledSubtitle>
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
                <TableCell>Nombre</TableCell>
                <TableCell>Precio Gas</TableCell>
                <TableCell>Precio Diesel</TableCell>
                <TableCell>Precio Híbrido</TableCell>
                <TableCell>Precio Eléctrico</TableCell>
                <TableCell>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {reparaciones.map(reparacion => (
                <TableRow key={reparacion.id}>
                  <TableCell>{reparacion.id}</TableCell>
                  <TableCell>{reparacion.nombre}</TableCell>
                  <TableCell>{reparacion.precioGas}</TableCell>
                  <TableCell>{reparacion.precioDiesel}</TableCell>
                  <TableCell>{reparacion.precioHibrid}</TableCell>
                  <TableCell>{reparacion.precioElectric}</TableCell>
                  <TableCell>
                    <Box display="flex" justifyContent="flex-start">
                      <ModifyButton variant="contained" onClick={() => handleModify(reparacion.id)}>
                        Modificar
                      </ModifyButton>
                      <DeleteButton variant="contained" onClick={() => handleDelete(reparacion.id)}>
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
            {`¿Está seguro que quiere borrar ${selectedReparacion?.nombre}?`}
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

export default ListarReparaciones;
