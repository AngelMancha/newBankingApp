window.onload = () => {
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
    "Sin etiqueta": "fa-tag",
    "Restauracion": "fa-utensils",
    "Ingresos": "fa-money-bill-wave",
    "Ocio": "fa-solid fa-ticket",
    "Viajes": "fa-plane",
    "Transporte": "fa-bus",
    "Multimedia": "fa-photo-video",
    "Regalos": "fa-gift",       // Added icon for "Regalos"
    "Caprichos": "fa-gem",      // Added icon for "Caprichos"
    "Coche": "fa-car",           // Added icon for "Coche"
     "Otros": "fa-shopping-cart",
};
const etiquetasColores = {
    "Sin etiqueta": "#D9D9D9",  // Muted Gray
    "Ocio": "#B5655C",         // Muted Red
    "Restauracion": "#D9A05B", // Muted Orange
    "Viajes": "#D9A5B3",       // Muted Pink
    "Ingresos": "#6FA8DC",     // Muted Light Blue
    "Transporte": "#A992B0",   // Muted Purple
    "Multimedia": "#D9C77B",   // Muted Yellow
    "Regalos": "#FF69B4",      // Light Pink
    "Caprichos": "#6FAF74",    // Pale Green
    "Coche": "#9370DB",        // Medium Purple
    "Otros": "#98FB98"       // Muted Green

};
let selectedExpenses = [];
let selectedIncomes = [];
let selectedRowForTag = null;
let monthlyExpensesChart = null;
let monthlyIncomesChart = null;

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

document.getElementById("toggleStatsButton").addEventListener("click", function () {
    const sections = [
        document.getElementById("grafico-barras-section-anual"),
        document.getElementById("monthlyExpensesTitle"),
        document.getElementById("monthly-expenses"),
        document.getElementById("monthlyIncomesTitle"),
        document.getElementById("monthly-incomes")
    ];

    sections.forEach(section => {
        section.classList.toggle("hidden");
    });

    // Cambiar el texto del botón
    this.textContent = this.textContent === "Mostrar estadísticas anuales"
        ? "Ocultar estadísticas anuales"
        : "Mostrar estadísticas anuales";
});




function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

function cargarOpcionesMes() {
    const monthFilter = document.getElementById('monthFilter');
    const yearFilter = document.getElementById('yearFilter');
    const selectedMonthCookie = getCookie('selectedMonth');
    const selectedYearCookie = getCookie('selectedYear');

    // Agregar opción "sin elección" al inicio del desplegable de meses
    const noSelectionOption = document.createElement('option');
    noSelectionOption.value = '';
    noSelectionOption.textContent = 'sin elección';
    monthFilter.appendChild(noSelectionOption);

    for (let month = 0; month < 12; month++) {
        const option = document.createElement('option');
        const date = new Date(2024, month);
        option.value = month;
        option.textContent = date.toLocaleString('default', { month: 'long' });
        if (selectedMonthCookie !== null && parseInt(selectedMonthCookie) === month) {
            option.selected = true;
        }
        monthFilter.appendChild(option);
    }

    [2024, 2025].forEach(year => {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year;
        if (selectedYearCookie !== null && parseInt(selectedYearCookie) === year) {
            option.selected = true;
        }
        yearFilter.appendChild(option);
    });

    if (!isNaN(selectedMonthCookie)) {
        fetchDataAndGenerateCharts();

    } else {
        console.log('No se ha seleccionado un mes');
        fetchDataAndGenerateChartsForYear();
    }

    fetchDataAndGenerateChartsAnuales();
    fetchMonthylyIncome();
    fetchMonthlyExpenses();


}

function filtrarPorMes() {
    const selectedMonth = parseInt(document.getElementById('monthFilter').value) + 1;
    const selectedYear = parseInt(document.getElementById('yearFilter').value);
    document.cookie = `selectedMonth=${selectedMonth};path=/;expires=${new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toUTCString()}`;
    document.cookie = `selectedYear=${selectedYear};path=/;expires=${new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toUTCString()}`;

    if (!isNaN(selectedMonth)) {
        fetchDataAndGenerateCharts();

    } else {
        console.log('No se ha seleccionado un mes');
        fetchDataAndGenerateChartsForYear();
    }

    fetchDataAndGenerateChartsAnuales();
    fetchMonthylyIncome();
    fetchMonthlyExpenses();
}

function fetchMonthylyIncome() {

    const montlyTilesUrl = 'http://localhost:8080/banking/get_payroll_month ';
    const requestBody = {
        year: getCookie('selectedYear')
    };

    Promise.all([
        fetch(montlyTilesUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        }).then(response => response.json())
    ])
    .then(([montlyTilesData]) => {
        createMonthlyIncomeGraph(montlyTilesData);
    })
}

function fetchMonthlyExpenses() {

    const montlyTilesUrl = 'http://localhost:8080/banking/get_expenses_month';
    const requestBody = {
        year: getCookie('selectedYear')
    };
    Promise.all([
        fetch(montlyTilesUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        }).then(response => response.json())
    ])
    .then(([montlyTilesData]) => {
        createMonthlyExpensesGraph(montlyTilesData);
    })
}

function fetchDataAndGenerateCharts(mesSeleccionado = null, anoSeleccionado = null) {
    const expensesUrl = 'http://localhost:8080/banking/get_expenses';
    const incomeUrl = 'http://localhost:8080/banking/get_income';

    const requestBody = {
        month: getCookie('selectedMonth'),
        year: getCookie('selectedYear')
    };

    Promise.all([
        fetch(expensesUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        }).then(response => response.json()),
        fetch(incomeUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
         }).then(response => response.json())
    ])
    .then(([expensesData, incomeData]) => {
        expensesData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));
        incomeData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));



        cargarDatos('gastos-lista', expensesData, 'expense');
        cargarDatos('ingresos-lista', incomeData, 'income');

        generarEstadisticas('grafico-gastos', expensesData, 'Gastos por Etiqueta (€)');
    })
    .catch(error => {
        console.error('Error al obtener los datos:', error);
    });
}

function fetchDataAndGenerateChartsForYear(mesSeleccionado = null, anoSeleccionado = null) {
    const expensesUrl = 'http://localhost:8080/banking/get_expenses_anuales';
    const incomeUrl = 'http://localhost:8080/banking/get_income';

    const requestBody = {
        year: getCookie('selectedYear')
    };

    Promise.all([
        fetch(expensesUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        }).then(response => response.json()),
        fetch(incomeUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
         }).then(response => response.json())
    ])
    .then(([expensesData, incomeData]) => {
        expensesData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));
        incomeData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));



        cargarDatos('gastos-lista', expensesData, 'expense');
        cargarDatos('ingresos-lista', incomeData, 'income');

        generarEstadisticas('grafico-gastos', expensesData, 'Gastos por Etiqueta (€)');
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

function createMonthlyExpensesGraph(data) {
    const selectedYear = document.getElementById('yearFilter').value;
    let totalGastoElement = document.getElementById('monthlyExpensesTitle');
    totalGastoElement.textContent = `Distribución mensual de gastos en ${selectedYear}:`;

    const monthlyExpensesContainer = document.getElementById('monthly-expenses');
    monthlyExpensesContainer.innerHTML = '<canvas id="monthlyExpensesChart" width="100" height="50"></canvas>';

    const ctx = document.getElementById('monthlyExpensesChart').getContext('2d');
    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    const expenses = months.map(month => Math.abs(data[month.toLowerCase()]));

    if (monthlyExpensesChart) {
        monthlyExpensesChart.destroy();
    }

    const isMobile = window.innerWidth <= 768; // Detect mobile screen size

    monthlyExpensesChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Expenses (€)',
                data: expenses,
                backgroundColor: '#b68b40',
                borderColor: '#122620',
                borderWidth: 1,
                borderRadius: 10 // Rounded corners for bars
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: isMobile ? 'y' : 'x', // Horizontal bars for mobile
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 200
                    }
                },
                x: {
                    barPercentage: 0.1,
                    categoryPercentage: 0.1
                }
            },
            plugins: {
                legend: {
                   display: false
                },
                datalabels: {
                    anchor: 'center', // Center the value inside the bar
                    align: isMobile ? 'end' : 'center', // Center for vertical bars
                    formatter: (value) => value === 0 ? '' : value.toFixed(2) + ' €',
                    color: '#122620',
                    font: {
                        weight: 'bold', // Makes the text bold
                        size: isMobile ? 12 : 13 // Adjust font size for mobile
                    },
                    rotation: isMobile ? 0 : -40 // No rotation for horizontal bars
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}
function createMonthlyIncomeGraph(data) {
    console.log('data:', data);
    const selectedYear = document.getElementById('yearFilter').value;
    let totalGastoElement = document.getElementById('monthlyIncomesTitle');
    totalGastoElement.textContent = `Distribución mensual de ingresos en ${selectedYear}:`;

    const isMobile = window.innerWidth <= 768; // Detect mobile screen size

    const monthlyIncomesContainer = document.getElementById('monthly-incomes');
    monthlyIncomesContainer.innerHTML = '<canvas id="monthlyIncomesChart" width="100" height="50"></canvas>';

    const ctx = document.getElementById('monthlyIncomesChart').getContext('2d');
    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    const expenses = months.map(month => Math.abs(data[month.toLowerCase()]));

    if (monthlyIncomesChart) {
        monthlyIncomesChart.destroy();
    }

    monthlyIncomesChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Incomes (€)',
                data: expenses,
                backgroundColor: '#122620',
                borderColor: '#b68b40',
                borderWidth: 1,
                borderRadius: 10 // Rounded corners for bars

            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: isMobile ? 'y' : 'x', // Horizontal bars for mobile

            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 200
                    }
                },
                x: {
                    barPercentage: 0.1,
                    categoryPercentage: 0.1
                }
            },
            plugins: {
                legend: {
                   display: false
                },
                datalabels: {
                    anchor: 'center', // Center the value inside the bar
                    align: isMobile ? 'end' : 'center', // Center for vertical bars
                    formatter: (value) => value === 0 ? '' : value.toFixed(2) + ' €',
                    color: '#b68b40',
                    font: {
                        weight: 'bold', // Makes the text bold
                        size: isMobile ? 12 : 13 // Adjust font size for mobile
                    },
                    rotation: isMobile ? 0 : -40 // No rotation for horizontal bars

                }
            }
        },
        plugins: [ChartDataLabels]
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

function darkenColor(color, percent) {
    const num = parseInt(color.slice(1), 16),
        amt = Math.round(2.55 * percent),
        R = (num >> 16) - amt,
        G = (num >> 8 & 0x00FF) - amt,
        B = (num & 0x0000FF) - amt;
    return `#${(0x1000000 + (R < 255 ? (R < 1 ? 0 : R) : 255) * 0x10000 + (G < 255 ? (G < 1 ? 0 : G) : 255) * 0x100 + (B < 255 ? (B < 1 ? 0 : B) : 255)).toString(16).slice(1).toUpperCase()}`;
}

function filtrarGastosPorEtiqueta(etiqueta, data) {
    const gastosFiltrados = data.filter(gasto => gasto.etiqueta === etiqueta);
    cargarDatos('gastos-lista', gastosFiltrados, 'expense');
}

// script.js
document.getElementById('uploadIcon').addEventListener('click', () => {
    document.getElementById('uploadForm').style.display = 'block';
});


document.getElementById('fileIcon').addEventListener('click', () => {
    document.getElementById('fileInput').click();
});



document.getElementById('uploadForm').addEventListener('submit', (event) => {
    event.preventDefault();

    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        // Capture form values
        const xlsConfigurationDto = {
            fechaCelda: document.getElementById('fechaCelda').value,
            conceptoCelda: document.getElementById('conceptoCelda').value,
            importeCelda: document.getElementById('importeCelda').value,
            headerRow: document.getElementById('headerRow').value
        };

        formData.append('xlsConfigurationDto', new Blob([JSON.stringify(xlsConfigurationDto)], { type: 'application/json' }));

        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/banking/upload', true);

        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                const percentComplete = (event.loaded / event.total) * 100;
                document.getElementById('progressBar').style.width = percentComplete + '%';
                document.getElementById('progressText').textContent = Math.round(percentComplete) + '%';
            }
        };

        xhr.onload = () => {
            if (xhr.status === 200) {
                location.reload();
            } else {
                document.getElementById('progressText').textContent = 'Error uploading file';
            }
            document.getElementById('uploadModal').style.display = 'none';
        };

        xhr.onerror = () => {
            document.getElementById('progressText').textContent = 'Error uploading file';
            document.getElementById('uploadModal').style.display = 'none';
        };

        document.getElementById('uploadModal').style.display = 'block';
        xhr.send(formData);
    }
});
document.getElementById('closeUploadModal').addEventListener('click', () => {
    document.getElementById('uploadModal').style.display = 'none';
});


function fetchDataAndGenerateChartsAnuales(mesSeleccionado = null, anoSeleccionado = null) {
    const expensesUrl = 'http://localhost:8080/banking/get_expenses_anuales';

    const requestBody = {
        month: getCookie('selectedMonth'),
        year: getCookie('selectedYear')
    };


    fetch(expensesUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    }).then(response => response.json())
    .then(data => {
        const expensesData = data; // Ensure data is an array
        if (Array.isArray(expensesData)) {
            console.log("LAS ANUALES SON: ");
            console.log(expensesData);
            expensesData.sort((a, b) => new Date(a.fechaOperacion) - new Date(b.fechaOperacion));
            generarEstadisticasAnuales('grafico-gastos-anual', expensesData, 'Gastos Anuales por Etiqueta (€)');
        } else {
            console.error('Error: expensesData is not an array');
        }
    })
    .catch(error => {
        console.error('Error al obtener los datos:', error);
    });
}

function generarEstadisticas(canvasId, data, label) {
    const canvasContainer = document.getElementById(canvasId).parentNode;
    canvasContainer.removeChild(document.getElementById(canvasId));

    const newCanvas = document.createElement('canvas');
    newCanvas.id = canvasId;
    canvasContainer.appendChild(newCanvas);

    const isMobile = window.innerWidth <= 768; // Detect mobile screen size

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

    const sortedData = etiquetas.map((etiqueta, index) => ({ etiqueta, monto: montos[index] }))
                                .sort((a, b) => b.monto - a.monto);
    const sortedEtiquetas = sortedData.map(item => item.etiqueta);
    const sortedMontos = sortedData.map(item => item.monto);

    let totalGastoElement = document.getElementById('totalGasto');

    if (!totalGastoElement) {
        totalGastoElement = document.createElement('div');
        totalGastoElement.id = 'totalGasto';
        totalGastoElement.style.textAlign = 'center';
        totalGastoElement.style.fontWeight = 'bold';
        totalGastoElement.style.marginBottom = '20px';
        totalGastoElement.style.fontSize = '1.7em';
        canvasContainer.insertBefore(totalGastoElement, newCanvas);
    }

    const selectedMonth = document.getElementById('monthFilter').value;
    const selectedYear = document.getElementById('yearFilter').value;
    let monthName = '';
    if (selectedMonth !== '') {
        monthName = new Date(2024, selectedMonth).toLocaleString('default', { month: 'long' });
    }
    totalGastoElement.textContent = `Gasto Total en ${monthName} ${selectedYear}: ${totalGasto.toFixed(2)}€`;

    const maxMonto = Math.max(...sortedMontos);
    const stepSize = 100;
    let maxY = Math.ceil(maxMonto / stepSize) * stepSize;

    if (maxMonto >= maxY - 20) {
        maxY += stepSize;
    }

    const backgroundColors = sortedEtiquetas.map(et => etiquetasColores[et] || '#ccc');
    const borderColors = backgroundColors.map(color => darkenColor(color, 20));

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: sortedEtiquetas,
            datasets: [{
                label: "",
                data: sortedMontos,
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderRadius: 10, // Rounded corners for labels
                borderWidth: 3
            }]
        },
        options:
         {
            indexAxis: isMobile ? 'y' : 'x', // Horizontal bars for mobile
            plugins: {
                legend: {
                    display: false
                },
                datalabels: {
                    anchor: 'center', // Center the value inside the bar
                    align: isMobile ? 'end' : 'center', // Center for vertical bars
                    formatter: (value) => value.toFixed(2) + ' €',
                    color: 'black',
                    font: {
                        weight: 'bold', // Makes the text bold
                        size: isMobile ? 12 : 16 // Adjust font size for mobile
                    },
                    rotation: isMobile ? 0 : 0 // No rotation for horizontal bars
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: maxY,
                    ticks: {
                        stepSize: stepSize
                    }
                }
            },
            onClick: (event, elements) => {
                if (elements.length > 0) {
                    const index = elements[0].index;
                    const etiquetaSeleccionada = sortedEtiquetas[index];
                    filtrarGastosPorEtiqueta(etiquetaSeleccionada, data);
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}

function generarEstadisticasAnuales(canvasId, data, label) {
    const canvasContainer = document.getElementById(canvasId).parentNode;
    canvasContainer.removeChild(document.getElementById(canvasId));

    const newCanvas = document.createElement('canvas');
    newCanvas.id = canvasId;
    canvasContainer.appendChild(newCanvas);

    const isMobile = window.innerWidth <= 768; // Detect mobile screen size

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

    const sortedData = etiquetas.map((etiqueta, index) => ({ etiqueta, monto: montos[index] }))
                                .sort((a, b) => b.monto - a.monto);
    const sortedEtiquetas = sortedData.map(item => item.etiqueta);
    const sortedMontos = sortedData.map(item => item.monto);

    let totalGastoElement = document.getElementById('totalGastoAnual');

    if (!totalGastoElement) {
        totalGastoElement = document.createElement('div');
        totalGastoElement.id = 'totalGastoAnual';
        totalGastoElement.style.textAlign = 'center';
        totalGastoElement.style.fontWeight = 'bold';
        totalGastoElement.style.marginBottom = '20px';
        totalGastoElement.style.fontSize = '1.5em';
        canvasContainer.insertBefore(totalGastoElement, newCanvas);
    }

    const selectedYear = document.getElementById('yearFilter').value;

    totalGastoElement.textContent = `Gasto total en ${selectedYear}: ${totalGasto.toFixed(2)}€`;

    const maxMonto = Math.max(...sortedMontos);
    const stepSize = 100;
    let maxY = Math.ceil(maxMonto / stepSize) * stepSize;

    if (maxMonto >= maxY - 20) {
        maxY += stepSize;
    }

    const backgroundColors = sortedEtiquetas.map(et => etiquetasColores[et] || '#ccc');
    const borderColors = backgroundColors.map(color => darkenColor(color, 20));

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: sortedEtiquetas,
            datasets: [{
                label: "",
                data: sortedMontos,
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                                borderRadius: 10, // Rounded corners for labels

                borderWidth: 3
            }]
        },
        options: {
            indexAxis: isMobile ? 'y' : 'x', // Horizontal bars for mobile
            responsive: true,

            plugins: {
                legend: {
                    display: false
                },
                datalabels: {
                    anchor: 'center', // Center the value inside the bar
                    align: isMobile ? 'end' : 'center', // Center for vertical bars
                    formatter: (value) => value.toFixed(2) + ' €',
                    color: 'black',
                    font: {
                        weight: 'bold', // Makes the text bold
                        size: isMobile ? 12 : 16 // Adjust font size for mobile
                    },
                    rotation: isMobile ? 0 : 0 // No rotation for horizontal bars
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: maxY,
                    ticks: {
                        stepSize: stepSize
                    }
                }
            },
            onClick: (event, elements) => {
                if (elements.length > 0) {
                    const index = elements[0].index;
                    const etiquetaSeleccionada = sortedEtiquetas[index];
                    filtrarGastosPorEtiqueta(etiquetaSeleccionada, data);
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}