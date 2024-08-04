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

const Reporte2 = () => {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mes, setMes] = useState(new Date().getMonth() + 1);
  const [year, setYear] = useState(new Date().getFullYear());

  const fetchData = async (mes, year) => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/autofix/citas/reportes/reporte2', {
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

  const getMonthName = (month, year) => {
    return new Date(year, month - 1).toLocaleString('es-ES', { month: 'long' });
  };

  const calculatePreviousMonth = (month, year) => {
    let newMonth = month - 1;
    let newYear = year;
    if (newMonth < 1) {
      newMonth = 12;
      newYear -= 1;
    }
    return { month: newMonth, year: newYear };
  };

  const mes1 = mes;
  const { month: mes2, year: year2 } = calculatePreviousMonth(mes, year);
  const { month: mes3, year: year3 } = calculatePreviousMonth(mes2, year2);

  const mes1Name = getMonthName(mes1, year);
  const mes2Name = getMonthName(mes2, year2);
  const mes3Name = getMonthName(mes3, year3);

  return (
    <StyledContainer>
      <Header>
        <BackButton variant="contained" onClick={() => navigate('/menu-reportes')}>
          Volver
        </BackButton>
        <StyledSubtitle>home &gt; menú reportes &gt; reporte 2</StyledSubtitle>
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
                <StyledTableCell>{mes1Name}</StyledTableCell>
                <StyledTableCell>Variación</StyledTableCell>
                <StyledTableCell>{mes2Name}</StyledTableCell>
                <StyledTableCell>Variación</StyledTableCell>
                <StyledTableCell>{mes3Name}</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data.map((row, index) => (
                <React.Fragment key={index}>
                  <TableRow>
                    <TableCell>{row.nombreReparacion}</TableCell>
                    <TableCell>{row.cantidadReparacion1}</TableCell>
                    <TableCell>{row.variacionCantidad2}%</TableCell>
                    <TableCell>{row.cantidadReparacion2}</TableCell>
                    <TableCell>{row.variacionCantidad3}%</TableCell>
                    <TableCell>{row.cantidadReparacion3}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell></TableCell>
                    <TableCell>{row.montoReparacion1}</TableCell>
                    <TableCell>{row.variacionMonto2}%</TableCell>
                    <TableCell>{row.montoReparacion2}</TableCell>
                    <TableCell>{row.variacionMonto3}%</TableCell>
                    <TableCell>{row.montoReparacion3}</TableCell>
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

export default Reporte2;
