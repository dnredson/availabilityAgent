const cron = require("node-cron");
const express = require("express");

app = express();


// CRON JOB EXECUTANDO DE UM EM UM MINUTO
cron.schedule("*/10 * * * *", function(){

 console.log("Executando a tarefa a cada 10 minutos")}
 );

app.listen(1313);