window.onload = () => {
    getData(new Date().toLocaleDateString());

    let richiedidati = document.getElementById('richiedidati');
    richiedidati.addEventListener('submit', (e) => {
        e.preventDefault();
        let day = document.getElementById('date').value;
        day = day.split('-');
        day = String(parseInt(day[1])) + '/' + String(parseInt(day[2])) + '/' + String(parseInt(day[0]));
        getData(day);
    });

    let cambiastatoStanza = document.getElementById('stanza');
    cambiastatoStanza.addEventListener('submit', (e) => {
        e.preventDefault();
        let timeValue = new Date().getHours() + ":" + new Date().getMinutes();
        let lightsValue = document.querySelector('#lights');
        lightsValue = lightsValue.checked ? "ON" : "OFF";
        let angleValue = document.getElementById('slider').value;
        const data = { time: timeValue, lights: lightsValue , angle: angleValue};
        fetch('address', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data =>
        getData(new Date().toLocaleDateString()))
        .catch(error => console.error(error));
    });

    //Slider
    let slider = document.getElementById('slider');
    let valore = document.getElementById('sliderlabel');
    slider.addEventListener("input", () => {
        const posizione = slider.value;
        valore.textContent = "Posizione Persiane:" + posizione + "%";
    });
}

function getData(day){
    day = day.split(/\//g);
    correctdate = day[1] + '-' + day[0] + '-' + day[2];
    const tabella = document.querySelector("table");
    const giornodati = document.getElementById("giornodati");
    const tbody = tabella.getElementsByTagName("tbody")[0];
    const errore = document.getElementById('errore');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/provajson/'+correctdate+'.json');
    xhr.onload = () => {
    tbody.innerHTML = "";
    if (xhr.status === 200) {
        const data = JSON.parse(xhr.response);
        tabella.style.visibility = "visible";
        giornodati.style.visibility = "visible";
        giornodati.innerText = "Dati del giorno: " + correctdate;
        data.forEach(elemento => {
            const riga = tbody.insertRow();
            const cellaOrario = riga.insertCell();
            const cellaLuci = riga.insertCell();
            const cellaPersiane = riga.insertCell();
            cellaOrario.innerText = elemento.time;
            cellaLuci.innerText = elemento.lights;
            cellaPersiane.innerText = elemento.angle;
            errore.innerText = "";
        });
    } else {
        console.error('Errore nella richiesta');
        errore.innerText = "Errore nella richiesta";
        tabella.style.visibility = "hidden";
        giornodati.style.visibility = "hidden";
    }
    };
    xhr.send();
}