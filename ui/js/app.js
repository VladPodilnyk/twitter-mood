'use strict';

require('dotenv').config();

function fetchTwitterMood() {  
    let url = "http://localhost:8080/status"

    fetch(url,  {
        mode: 'cors',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(data => document.getElementById("title").textContent = data.mood)
    .catch((error) => {
        console.error('Error:', error)
    })
}