import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import Yourad from './components/Yourad';
import AutoFix from './components/AutoFix';
import MenuReparaciones from './components/MenuReparaciones';

function App() {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/papa' element={<Yourad />} />
        <Route path='/autofix' element={<AutoFix />} />
        <Route path='/menu-reparaciones' element={<MenuReparaciones />} />
      </Routes>
    </Router>
  );
}

export default App;
