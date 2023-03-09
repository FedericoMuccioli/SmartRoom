window.onload = () => {
    getData(new Date().toLocaleDateString());

    let richiedidati = document.getElementById('richiedidati');
    let day = document.getElementById('date');
    day.value = new Date().toISOString().substring(0, 10);
    richiedidati.addEventListener('submit', (e) => {
        e.preventDefault();
        let value = day.value;
        value = value.split('-');
        value = String(parseInt(value[1])) + '/' + String(parseInt(value[2])) + '/' + String(parseInt(value[0]));
        getData(value);
    });

    let cambiastatoStanza = document.getElementById('stanza');
    cambiastatoStanza.addEventListener('submit', (e) => {
        e.preventDefault();
        let lightsValue = document.querySelector('#lights');
        lightsValue = lightsValue.checked ? "ON" : "OFF";
        let positionValue = document.getElementById('slider').value;
        const data = {lights: lightsValue , position: positionValue};
        fetch('http://localhost:8080/IoT', {
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
    const [giorno, mese, anno] = correctdate.split('-');
    const dataFormattata = `${giorno.padStart(2, '0')}-${mese.padStart(2, '0')}-${anno}`;
    const tabella = document.querySelector("table");
    const giornodati = document.getElementById("giornodati");
    const tbody = tabella.getElementsByTagName("tbody")[0];
    const errore = document.getElementById('errore');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/IoT?file='+dataFormattata+'.json');
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
            cellaLuci.innerText = elemento.lights == "1" ? "Accese" : "Spente";
            cellaPersiane.innerText = elemento.position + "%";
            errore.innerText = "";
        });
    } else {
        errore.innerText = "Errore nella richiesta: dati non disponibili";
        tabella.style.visibility = "hidden";
        giornodati.style.visibility = "hidden";
    }
    };
    xhr.send();
}