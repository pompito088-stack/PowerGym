document.addEventListener('DOMContentLoaded', function () {
    const cuerpoTabla = document.getElementById('cuerpo-tabla-clientes');
    if (!cuerpoTabla) {
        return;
    }

    const filas = Array.from(cuerpoTabla.querySelectorAll('tr[data-cliente]'));
    const paginacion = document.getElementById('paginacion-clientes');
    const infoPaginacion = document.getElementById('info-paginacion');
    const selectorTamano = document.getElementById('tamano-pagina');

    if (filas.length === 0 || !paginacion || !infoPaginacion || !selectorTamano) {
        return;
    }

    let tamanoPagina = parseInt(selectorTamano.value, 10);
    let paginaActual = 1;

    function totalPaginas() {
        return Math.max(1, Math.ceil(filas.length / tamanoPagina));
    }

    function crearItem(etiqueta, pagina, opciones) {
        opciones = opciones || {};
        const li = document.createElement('li');
        li.className = 'page-item'
            + (opciones.activo ? ' active' : '')
            + (opciones.deshabilitado ? ' disabled' : '');

        const enlace = document.createElement('a');
        enlace.className = 'page-link';
        enlace.href = '#';
        enlace.textContent = etiqueta;
        enlace.addEventListener('click', function (evento) {
            evento.preventDefault();
            if (!opciones.deshabilitado) {
                mostrarPagina(pagina);
            }
        });

        li.appendChild(enlace);
        return li;
    }

    function renderizarControles(total) {
        paginacion.innerHTML = '';
        paginacion.appendChild(crearItem('Anterior', paginaActual - 1, { deshabilitado: paginaActual === 1 }));
        for (let pagina = 1; pagina <= total; pagina++) {
            paginacion.appendChild(crearItem(String(pagina), pagina, { activo: pagina === paginaActual }));
        }
        paginacion.appendChild(crearItem('Siguiente', paginaActual + 1, { deshabilitado: paginaActual === total }));
    }

    function renderizarInfo(inicio, fin) {
        const desde = inicio + 1;
        const hasta = Math.min(fin, filas.length);
        infoPaginacion.textContent = 'Mostrando ' + desde + '-' + hasta + ' de ' + filas.length + ' clientes';
    }

    function mostrarPagina(pagina) {
        const total = totalPaginas();
        paginaActual = Math.min(Math.max(pagina, 1), total);

        const inicio = (paginaActual - 1) * tamanoPagina;
        const fin = inicio + tamanoPagina;

        filas.forEach(function (fila, indice) {
            fila.classList.toggle('d-none', indice < inicio || indice >= fin);
        });

        renderizarControles(total);
        renderizarInfo(inicio, fin);
    }

    selectorTamano.addEventListener('change', function () {
        tamanoPagina = parseInt(selectorTamano.value, 10);
        mostrarPagina(1);
    });

    mostrarPagina(1);
});
