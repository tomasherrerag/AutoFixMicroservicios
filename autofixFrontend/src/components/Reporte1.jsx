import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, Typography, Box, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, MenuItem, Select, TextField } from '@mui/material';
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

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  position: 'sticky',
  top: 0,
  backgroundColor: '#ffffff',
  zIndex: 1,
}));

const Reporte1 = () => {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mes, setMes] = useState(new Date().getMonth() + 1);
  const [year, setYear] = useState(new Date().getFullYear());

  const fetchData = async (mes, year) => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/autofix/citas/reportes/reporte1', {
        params: { mes, year }
      });
      setData(response.data);
    } catch (error) {
      console.error('Error fetching report data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(mes, year);
  }, [mes, year]);

  const handleMesChange = (event) => {
    setMes(event.target.value);
  };

  const handleYearChange = (event) => {
    setYear(event.target.value);
  };

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-reportes')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reportes &gt; reporte 1</StyledSubtitle>
        <StyledTitle>AutoFix Calculator</StyledTitle>
      </Header>
      <Box mt={8} mb={2} display="flex" justifyContent="center" alignItems="center">
        <Select value={mes} onChange={handleMesChange}>
          {Array.from({ length: 12 }, (_, index) => (
            <MenuItem key={index + 1} value={index + 1}>
              {new Date(0, index).toLocaleString('es-ES', { month: 'long' })}
            </MenuItem>
          ))}
        </Select>
        <TextField
          type="number"
          value={year}
          onChange={handleYearChange}
          label="Año"
          style={{ marginLeft: '16px' }}
        />
      </Box>
      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <StyledTableCell>Nombre Reparación</StyledTableCell>
                <StyledTableCell>Sedan</StyledTableCell>
                <StyledTableCell>Hatchback</StyledTableCell>
                <StyledTableCell>SUV</StyledTableCell>
                <StyledTableCell>Pickup</StyledTableCell>
                <StyledTableCell>Furgoneta</StyledTableCell>
                <StyledTableCell>Total</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data.map((row, index) => (
                <React.Fragment key={index}>
                  <TableRow>
                    <TableCell rowSpan={2}>{row.nombreReparacion}</TableCell>
                    <TableCell>{row.montoSedan}</TableCell>
                    <TableCell>{row.montoHatchback}</TableCell>
                    <TableCell>{row.montoSUV}</TableCell>
                    <TableCell>{row.montoPickup}</TableCell>
                    <TableCell>{row.montoFurgoneta}</TableCell>
                    <TableCell>{row.montoTotal}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>{row.cantSedan}</TableCell>
                    <TableCell>{row.cantHatchback}</TableCell>
                    <TableCell>{row.cantSUV}</TableCell>
                    <TableCell>{row.cantPickup}</TableCell>
                    <TableCell>{row.cantFurgoneta}</TableCell>
                    <TableCell>{row.cantTotal}</TableCell>
                  </TableRow>
                </React.Fragment>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </StyledContainer>
  );
};

export default Reporte1;
