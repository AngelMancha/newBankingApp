window.onload = () => {
    fetchDataAndGenerateCharts();
    cargarOpcionesMes();

    document.getElementById('mergeButton').addEventListener('click', () => {
        showMergeModal();
    });

    document.getElementById('confirmMergeButton').addEventListener('click', () => {
        mergePayments();
    });

    document.querySelector('.close').addEventListener('click', () => {
        document.getElementById('mergeModal').style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target == document.getElementById('mergeModal')) {
            document.getElementById('mergeModal').style.display = 'none';
        }
    });
};


function showMergeModal() {
    if (selectedExpenses.length === 0 || selectedIncomes.length === 0) {
        alert('Seleccione al menos un gasto y uno o más ingresos para fusionar.');
        return;
    }

    const mergeDetails = document.getElementById('mergeDetails');
    mergeDetails.innerHTML = `
        <h3>Gasto Seleccionado:</h3>
        <p>Concepto: ${selectedExpenses[0].concepto}</p>
        <p>Importe: ${selectedExpenses[0].importe}€</p>
        <h3>Ingresos Seleccionados:</h3>
        ${selectedIncomes.map(income => `
            <p>Concepto: ${income.concepto}</p>
            <p>Importe: ${income.importe}€</p>
        `).join('')}
    `;

    document.getElementById('mergeModal').style.display = 'block';
}

const etiquetasIconos = {
    "Restauracion": "fa-utensils",
    "Empresa": "fa-building",
    "Otros": "fa-shopping-cart",
    "Ingresos": "fa-money-bill-wave",
    "Ocio": "fa-solid fa-ticket",
    "Viajes": "fa-plane",
    "Transporte": "fa-bus",
    "Multimedia": "fa-photo-video"  // Added icon for multimedia


};

// Función para obtener una cookie por su nombre
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

// Cargar las opciones de mes en el desplegable
function cargarOpcionesMes() {
    const monthFilter = document.getElementById('monthFilter');
    const currentYear = new Date().getFullYear();
    const selectedMonthCookie = getCookie('selectedMonth');

    for (let month = 0; month < 12; month++) {
        const option = document.createElement('option');
        const date = new Date(currentYear, month);
        option.value = month;
        option.textContent = date.toLocaleString('default', { month: 'long' });
        if (selectedMonthCookie !== null && parseInt(selectedMonthCookie) === month) {
            option.selected = true;
        }
        monthFilter.appendChild(option);
    }
    if (selectedMonthCookie !== null) {
        fetchDataAndGenerateCharts(parseInt(selectedMonthCookie));
    }
}


let selectedExpense = null;
let selectedIncome = null;
let selectedExpenseData = null;
let selectedIncomeData = null;
let selectedExpenses = [];
let selectedIncomes = [];
let selectedRowForTag = null;

const etiquetasColores = {
    "Ocio": "#FF6F61",          // Stronger Pastel Red
    "Empresa": "#AEC6CF",       // Stronger Pastel Blue
    "Restauracion": "#FFB347",  // Stronger Pastel Orange
    "Viajes": "#FFC0CB",        // Stronger Pastel Yellow
    "Otros": "#77DD77",  // Stronger Pastel Green
    "Ingresos": "#89CFF0",// Stronger Pastel Blue
    "Transporte": "#C39BD3",     // Stronger Pastel Purple
    "Multimedia": "#FFD700"     // Stronger Pastel Gold
};


// Filtrar datos por el mes seleccionado
function filtrarPorMes() {
    const selectedMonth = parseInt(document.getElementById('monthFilter').value);
    document.cookie = `selectedMonth=${selectedMonth};path=/;expires=${new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toUTCString()}`;
    fetchDataAndGenerateCharts(selectedMonth);
}

function fetchDataAndGenerateCharts(mesSeleccionado = null) {
    const expensesUrl = 'http://localhost:8080/banking/get_expenses';
    const incomeUrl = 'http://localhost:8080/banking/get_income';

    Promise.all([
        fetch(expensesUrl).then(response => response.json()),
        fetch(incomeUrl).then(response => response.json())
    ])
    .then(([expensesData, incomeData]) => {
        // Ordenar datos por fecha
        expensesData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));
        incomeData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));

        // Filtrar por mes si se seleccionó uno
        if (mesSeleccionado !== null) {
            expensesData = expensesData.filter(gasto => new Date(gasto.fechaOperacion).getMonth() === mesSeleccionado);
            incomeData = incomeData.filter(ingreso => new Date(ingreso.fechaOperacion).getMonth() === mesSeleccionado);
        }

        // Cargar solo los datos filtrados
        cargarGastos(expensesData);
        cargarIngresos(incomeData);



        // Actualizar gráficos solo con los datos de gastos filtrados
        generarEstadisticas('grafico-gastos', expensesData, 'Gastos por Etiqueta (€)');
        generarGraficoCircular('grafico-circular', expensesData, 'Distribución de Gastos');
    })
    .catch(error => {
        console.error('Error al obtener los datos:', error);
    });
}

function cargarGastos(data) {
    const gastosLista = document.getElementById('gastos-lista');
    gastosLista.innerHTML = '';  // Limpia la lista antes de cargar nuevos datos
    data.forEach((gasto, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(gasto.fechaOperacion).toLocaleDateString('es-ES', { day: 'numeric', month: 'short' })}</td>
            <td>${gasto.concepto}</td>
            <td>${gasto.importe >= 0 ? '+' : ''}${gasto.importe.toFixed(2)}€</td>
            <td>
                <div class="etiqueta-container" style="display: flex; align-items: center; justify-content: center; background-color: ${etiquetasColores[gasto.etiqueta] || '#ccc'}; border-radius: 10px; padding: 3px 8px; color: white; cursor: pointer;">
                    <i class="fas ${etiquetasIconos[gasto.etiqueta] || 'fa-tag'}"></i>
                    <select onchange="updateTag(this, ${index}, 'expense')" style="border: none; background-color: transparent; color: inherit;">
                        ${generateEtiquetaOptions(gasto.etiqueta)}
                    </select>
                </div>
            </td>
        `;

        // Muestra el desplegable inmediatamente al hacer clic en la celda
        const etiquetaContainer = row.querySelector('.etiqueta-container');
        etiquetaContainer.addEventListener('click', (e) => {
            const selectElement = etiquetaContainer.querySelector('select');
            selectElement.style.display = 'inline-block';
            selectElement.focus(); // Opcional: enfocar el select automáticamente
        });

        row.addEventListener('click', () => selectRow(row, 'expense', data[index]));
        gastosLista.appendChild(row);
    });
}
function cargarIngresos(data) {
    const ingresosLista = document.getElementById('ingresos-lista');
    ingresosLista.innerHTML = '';  // Limpia la lista antes de cargar nuevos datos
    data.forEach((ingreso, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(ingreso.fechaOperacion).toLocaleDateString('es-ES', { day: 'numeric', month: 'short' })}</td>
            <td>${ingreso.concepto}</td>
            <td>${ingreso.importe >= 0 ? '+' : ''}${ingreso.importe.toFixed(2)}€</td>
            <td>
                <div class="etiqueta-container" style="display: flex; align-items: center; justify-content: center; background-color: ${etiquetasColores[ingreso.etiqueta] || '#ccc'}; border-radius: 10px; padding: 3px 8px; color: white; cursor: pointer;">
                    <i class="fas ${etiquetasIconos[ingreso.etiqueta] || 'fa-tag'}"></i>
                    <select onchange="updateTag(this, ${index}, 'income')" style="border: none; background-color: transparent; color: inherit;">
                        ${generateEtiquetaOptions(ingreso.etiqueta)}
                    </select>
                </div>
            </td>
        `;

        // Muestra el desplegable inmediatamente al hacer clic en la celda
        const etiquetaContainer = row.querySelector('.etiqueta-container');
        etiquetaContainer.addEventListener('click', (e) => {
            const selectElement = etiquetaContainer.querySelector('select');
            selectElement.style.display = 'inline-block';
            selectElement.focus(); // Opcional: enfocar el select automáticamente
        });

        row.addEventListener('click', () => selectRow(row, 'income', data[index]));
        ingresosLista.appendChild(row);
    });
}

function toggleDropdown(iconElement) {
    const selectElement = iconElement.nextElementSibling;
    selectElement.style.display = selectElement.style.display === 'none' ? 'inline-block' : 'none';
}

function generateEtiquetaOptions(selectedEtiqueta) {
    return Object.keys(etiquetasColores).map(etiqueta => {
        return `<option value="${etiqueta}" ${etiqueta === selectedEtiqueta ? 'selected' : ''} style="background-color: ${etiquetasColores[etiqueta] || '#ccc'}; color: black;">${etiqueta}</option>`;
    }).join('');
}


 function selectRow(row, type, data) {

     selectedRowForTag = data;
     console.log('selectedRowForTag:', selectedRowForTag);
     if (type === 'expense') {
         if (selectedExpenses.includes(data)) {
             selectedExpenses = selectedExpenses.filter(expense => expense !== data);
             row.classList.remove('selected');
         } else {
             selectedExpenses.push(data);
             row.classList.add('selected');
         }
     } else if (type === 'income') {
         if (selectedIncomes.includes(data)) {
             selectedIncomes = selectedIncomes.filter(income => income !== data);
             row.classList.remove('selected');
         } else {
             selectedIncomes.push(data);
             row.classList.add('selected');
         }
     }
 }

function mergePayments() {
    const mergeData = {
        operacionGasto: {
            "fechaOperacion": new Date(selectedExpenses[0].fechaOperacion).toISOString().replace('Z', '') + 'Z',
            "importe": parseFloat(selectedExpenses[0].importe.toString()).toFixed(2).toString(),
            "saldo": parseFloat(selectedExpenses[0].saldo.toString()).toFixed(2).toString(),
            "concepto": selectedExpenses[0].concepto + "",
            "etiqueta": selectedExpenses[0].etiqueta
        },
        operacionesIngreso: selectedIncomes.map(income => ({
            "fechaOperacion": new Date(income.fechaOperacion).toISOString().replace('Z', '') + 'Z',
            "importe": parseFloat(income.importe.toString()).toFixed(2).toString(),
            "saldo": parseFloat(income.saldo.toString()).toFixed(2).toString(),
            "concepto": income.concepto + "",
            "etiqueta": income.etiqueta
        }))
    };

    console.log('Request body:', JSON.stringify(mergeData));
    fetch('http://localhost:8080/banking/adjust_payment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(mergeData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Respuesta del servidor:', data);
        document.getElementById('mergeModal').style.display = 'none';
        location.reload();  // Reload the page
    })
    .catch(error => {
        console.error('Error al enviar la fusión de pagos:', error);
        alert('Hubo un error al enviar la fusión de pagos.');
    });
}

function updateTag(newTag, index, type) {
    console.log('selectedRowForTag:', selectedRowForTag.etiqueta);

    const changeTagData = {
        "fechaOperacion": new Date(selectedRowForTag.fechaOperacion).toISOString().replace('Z', '') + 'Z',
        "importe": parseFloat(selectedRowForTag.importe.toString()).toFixed(2).toString(),
        "saldo": parseFloat(selectedRowForTag.saldo.toString()).toFixed(2).toString(),
        "concepto": selectedRowForTag.concepto + "",
        "etiqueta": newTag.value
    };

    console.log('Cambiando etiqueta:', changeTagData);
    fetch('http://localhost:8080/banking/change_tag', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(changeTagData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Etiqueta cambiada con éxito:', data);

        location.reload();  // Reload the page
    })
    .catch(error => {
        console.error('Error al cambiar la etiqueta:', error);
        alert('Hubo un error al cambiar la etiqueta.');
    });
}

function generarEstadisticas(canvasId, data, label) {
    const canvasContainer = document.getElementById(canvasId).parentNode;
    canvasContainer.removeChild(document.getElementById(canvasId));

    const newCanvas = document.createElement('canvas');
    newCanvas.id = canvasId;
    canvasContainer.appendChild(newCanvas);

    const ctx = newCanvas.getContext('2d');
    const etiquetas = [];
    const montos = [];
    let totalGasto = 0;

    data.forEach(item => {
        const index = etiquetas.indexOf(item.etiqueta);
        const importeAbs = Math.abs(item.importe);
        totalGasto += importeAbs;
        if (index === -1) {
            etiquetas.push(item.etiqueta);
            montos.push(importeAbs);
        } else {
            montos[index] += importeAbs;
        }
    });

    let totalGastoElement = document.getElementById('totalGasto');
    if (!totalGastoElement) {
        totalGastoElement = document.createElement('div');
        totalGastoElement.id = 'totalGasto';
        totalGastoElement.style.textAlign = 'center';
        totalGastoElement.style.fontWeight = 'bold';
        canvasContainer.insertBefore(totalGastoElement, newCanvas);
    }
    totalGastoElement.textContent = `Gasto Total: ${totalGasto.toFixed(2)}€`;

    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: etiquetas,
            datasets: [{
                label: "",
                data: montos,
                backgroundColor: etiquetas.map(et => etiquetasColores[et] || '#ccc'),
                borderColor: etiquetas.map(et => etiquetasColores[et] || '#999'),
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                datalabels: {
                    anchor: 'end',
                    align: 'top',
                    formatter: (value) => value.toFixed(2) + '€',
                    color: 'black'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            onClick: (event, elements) => {
                if (elements.length > 0) {
                    const index = elements[0].index;
                    const etiquetaSeleccionada = etiquetas[index];
                    filtrarGastosPorEtiqueta(etiquetaSeleccionada, data);
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}

function filtrarGastosPorEtiqueta(etiqueta, data) {
    const gastosFiltrados = data.filter(gasto => gasto.etiqueta === etiqueta);
    cargarGastos(gastosFiltrados);
}
function generarGraficoCircular(canvasId, data, label) {
    const canvasContainer = document.getElementById(canvasId).parentNode;
    canvasContainer.removeChild(document.getElementById(canvasId));

    const newCanvas = document.createElement('canvas');
    newCanvas.id = canvasId;
    canvasContainer.appendChild(newCanvas);

    const ctx = newCanvas.getContext('2d');
    const etiquetas = [];
    const montos = [];

    data.forEach(item => {
        const index = etiquetas.indexOf(item.etiqueta);
        const importeAbs = Math.abs(item.importe);
        if (index === -1) {
            etiquetas.push(item.etiqueta);
            montos.push(importeAbs);
        } else {
            montos[index] += importeAbs;
        }
    });

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: etiquetas,
            datasets: [{
                label: label,
                data: montos,
                backgroundColor: etiquetas.map(et => etiquetasColores[et] || '#ccc')
            }]
        }
    });
}
