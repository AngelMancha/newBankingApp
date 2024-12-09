window.onload = () => {
    fetchDataAndGenerateCharts();
    cargarOpcionesMes();

    document.getElementById('mergeButton').addEventListener('click', showMergeModal);
    document.getElementById('confirmMergeButton').addEventListener('click', mergePayments);
    document.querySelector('.close').addEventListener('click', () => {
        document.getElementById('mergeModal').style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target == document.getElementById('mergeModal')) {
            document.getElementById('mergeModal').style.display = 'none';
        }
    });
};

const etiquetasIconos = {
    "Restauracion": "fa-utensils",
    "Empresa": "fa-building",
    "Otros": "fa-shopping-cart",
    "Ingresos": "fa-money-bill-wave",
    "Ocio": "fa-solid fa-ticket",
    "Viajes": "fa-plane",
    "Transporte": "fa-bus",
    "Multimedia": "fa-photo-video"
};

const etiquetasColores = {
    "Ocio": "#FF6F61",
    "Empresa": "#AEC6CF",
    "Restauracion": "#FFB347",
    "Viajes": "#FFC0CB",
    "Otros": "#77DD77",
    "Ingresos": "#89CFF0",
    "Transporte": "#C39BD3",
    "Multimedia": "#FFD700"
};


let selectedExpenses = [];
let selectedIncomes = [];
let selectedRowForTag = null;


function showMergeModal() {
    if (selectedExpenses.length === 0 || selectedIncomes.length === 0) {
        alert('Seleccione al menos un gasto y uno o más ingresos para fusionar.');
        return;
    }

const mergeDetails = document.getElementById('mergeDetails');
mergeDetails.innerHTML = `
    <h3>Gastos Seleccionados:</h3>
    ${selectedExpenses.map(expense => `
        <p>Concepto: ${expense.concepto}</p>
        <p>Importe: ${expense.importe}€</p>
    `).join('')}
    <h3>Ingresos Seleccionados:</h3>
    ${selectedIncomes.map(income => `
        <p>Concepto: ${income.concepto}</p>
        <p>Importe: ${income.importe}€</p>
    `).join('')}
`;

    document.getElementById('mergeModal').style.display = 'block';
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

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
        expensesData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));
        incomeData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));

        if (mesSeleccionado !== null) {
            expensesData = expensesData.filter(gasto => new Date(gasto.fechaOperacion).getMonth() === mesSeleccionado);
            incomeData = incomeData.filter(ingreso => new Date(ingreso.fechaOperacion).getMonth() === mesSeleccionado);
        }

        cargarDatos('gastos-lista', expensesData, 'expense');
        cargarDatos('ingresos-lista', incomeData, 'income');

        generarEstadisticas('grafico-gastos', expensesData, 'Gastos por Etiqueta (€)');
        generarGraficoCircular('grafico-circular', expensesData, 'Distribución de Gastos');
    })
    .catch(error => {
        console.error('Error al obtener los datos:', error);
    });
}

function cargarDatos(listaId, data, type) {
    const lista = document.getElementById(listaId);
    lista.innerHTML = '';
    data.forEach((item, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(item.fechaOperacion).toLocaleDateString('es-ES', { day: 'numeric', month: 'short' })}</td>
            <td>${item.concepto}</td>
            <td>${item.importe >= 0 ? '+' : ''}${item.importe.toFixed(2)}€</td>
            <td>
                <div class="etiqueta-container" style="display: flex; align-items: center; justify-content: center; background-color: ${etiquetasColores[item.etiqueta] || '#ccc'}; border-radius: 10px; padding: 3px 8px; color: white; cursor: pointer;">
                    <i class="fas ${etiquetasIconos[item.etiqueta] || 'fa-tag'}"></i>
                    <select onchange="updateTag(this, ${index}, '${type}')" style="border: none; background-color: transparent; color: inherit;">
                        ${generateEtiquetaOptions(item.etiqueta)}
                    </select>
                </div>
            </td>
        `;

        const etiquetaContainer = row.querySelector('.etiqueta-container');
        etiquetaContainer.addEventListener('click', () => {
            const selectElement = etiquetaContainer.querySelector('select');
            selectElement.style.display = 'inline-block';
            selectElement.focus();
        });

        row.addEventListener('click', () => selectRow(row, type, data[index]));
        lista.appendChild(row);
    });
}

function generateEtiquetaOptions(selectedEtiqueta) {
    return Object.keys(etiquetasColores).map(etiqueta => {
        return `<option value="${etiqueta}" ${etiqueta === selectedEtiqueta ? 'selected' : ''} style="background-color: ${etiquetasColores[etiqueta] || '#ccc'}; color: black;">${etiqueta}</option>`;
    }).join('');
}

function selectRow(row, type, data) {
    selectedRowForTag = data;
    if (type === 'expense') {
        toggleSelection(selectedExpenses, data, row);
    } else if (type === 'income') {
        toggleSelection(selectedIncomes, data, row);
    }
}

function toggleSelection(array, data, row) {
    if (array.includes(data)) {
        array.splice(array.indexOf(data), 1);
        row.classList.remove('selected');
    } else {
        array.push(data);
        row.classList.add('selected');
    }
}
function mergePayments() {
    const mergeData = {
        operacionesGasto: selectedExpenses.map(formatOperacion),// Mapping all "gasto" operations
        operacionesIngreso: selectedIncomes.map(formatOperacion)
    };

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
        document.getElementById('mergeModal').style.display = 'none';
        location.reload();
    })
    .catch(error => {
        console.error('Error al enviar la fusión de pagos:', error);
        alert('Hubo un error al enviar la fusión de pagos.');
    });
}
function formatOperacion(operacion) {
    return {
        fechaOperacion: new Date(operacion.fechaOperacion).toISOString().replace('Z', '') + 'Z',
        importe: parseFloat(operacion.importe.toString()).toFixed(2).toString(),
        saldo: parseFloat(operacion.saldo.toString()).toFixed(2).toString(),
        concepto: operacion.concepto + "",
        etiqueta: operacion.etiqueta
    };
}

function updateTag(newTag, index, type) {
    const changeTagData = formatOperacion(selectedRowForTag);
    changeTagData.etiqueta = newTag.value;

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
        location.reload();
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

    new Chart(ctx, {
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

function filtrarGastosPorEtiqueta(etiqueta, data) {
    const gastosFiltrados = data.filter(gasto => gasto.etiqueta === etiqueta);
    cargarDatos('gastos-lista', gastosFiltrados, 'expense');
}