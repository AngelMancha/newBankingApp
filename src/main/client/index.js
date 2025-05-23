const express = require('express');
const path = require('path');
const punycode = require('punycode'); // Desde el módulo npm


const app = express();

// Sirve archivos estáticos desde la carpeta 'www'
app.use(express.static(path.join(__dirname, 'www')));

// Configura una ruta que sirva un archivo HTML por defecto si es necesario
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'www', 'index.html'));
});

// Inicia el servidor en el puerto 3000
app.listen(3000, () => {
  console.log('Server running at http://localhost:3000/');
});
