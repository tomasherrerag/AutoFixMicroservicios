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

const ListarMarcas = () => {
  const navigate = useNavigate();
  const [marcas, setMarcas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selectedMarca, setSelectedMarca] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/autofix/marcas/')
      .then(response => {
        setMarcas(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        setLoading(false);
      });
  }, []);

  const handleModify = (id) => {
    navigate(`/modificar-marca/${id}`);
  };

  const handleDelete = (id) => {
    setSelectedMarca(marcas.find(marca => marca.id === id));
    setOpen(true);
  };

  const handleConfirmDelete = () => {
    axios.delete(`http://localhost:8080/autofix/marcas/${selectedMarca.id}`)
      .then(response => {
        console.log(response.data);
        setMarcas(marcas.filter(marca => marca.id !== selectedMarca.id));
        setOpen(false);
        setSelectedMarca(null);
      })
      .catch(error => {
        console.error('Error deleting marca:', error);
      });
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedMarca(null);
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-marcas')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú marcas &gt; listar marcas</StyledSubtitle>
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
                <TableCell>Número de Bonos</TableCell>
                <TableCell>Monto del Bono</TableCell>
                <TableCell>Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {marcas.map(marca => (
                <TableRow key={marca.id}>
                  <TableCell>{marca.id}</TableCell>
                  <TableCell>{marca.nombre}</TableCell>
                  <TableCell>{marca.numBonos}</TableCell>
                  <TableCell>{marca.montoBono}</TableCell>
                  <TableCell>
                    <Box display="flex" justifyContent="flex-start">
                      <ModifyButton variant="contained" onClick={() => handleModify(marca.id)}>
                        Modificar
                      </ModifyButton>
                      <DeleteButton variant="contained" onClick={() => handleDelete(marca.id)}>
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
            {`¿Está seguro que quiere borrar ${selectedMarca?.nombre}?`}
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

export default ListarMarcas;
