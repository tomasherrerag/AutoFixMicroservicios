import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AutoFix from './components/AutoFix';
import MenuReparaciones from './components/MenuReparaciones';
import MenuMarcas from './components/MenuMarcas';
import MenuVehiculos from './components/MenuVehiculos';
import MenuReportes from './components/MenuReportes';
import MenuCitas from './components/MenuCitas';
import ListarReparaciones from './components/ListarReparaciones';
import ModificarReparacion from './components/ModificarReparacion';
import AgregarReparacion from './components/AgregarReparacion';
import ListarMarcas from './components/ListarMarcas'; 
import ModifyMarca from './components/ModificarMarca';
import AgregarMarca from './components/AgregarMarca';
import ListarVehiculos from './components/ListarVehiculos.jsx';
import ModificarVehiculo from './components/ModificarVehiculo.jsx';
import AgregarVehiculo from './components/AgregarVehiculo.jsx';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<AutoFix />} />
        <Route path='/menu-reparaciones' element={<MenuReparaciones />} />
        <Route path='/menu-marcas' element={<MenuMarcas />} />
        <Route path='/menu-vehiculos' element={<MenuVehiculos />} />
        <Route path='/menu-reportes' element={<MenuReportes />} />
        <Route path='/menu-citas' element={<MenuCitas />} />
        <Route path='/listar-reparaciones' element={<ListarReparaciones />} />
        <Route path='/modificar-reparacion/:id' element={<ModificarReparacion />} />
        <Route path='/agregar-reparacion' element={<AgregarReparacion />} />
        <Route path='/listar-marcas' element={<ListarMarcas />} /> 
        <Route path='/modificar-marca/:id' element={<ModifyMarca />} /> 
        <Route path='/agregar-marca' element={<AgregarMarca />} /> 
        <Route path='/listar-vehiculos' element={<ListarVehiculos />} /> 
        <Route path='/modificar-vehiculo/:id' element={<ModificarVehiculo />} />
        <Route path='/agregar-vehiculo' element={<AgregarVehiculo />} />
      </Routes>
    </Router>
  );
}

export default App;
