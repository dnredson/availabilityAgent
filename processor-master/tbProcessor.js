"use strict";

const fs = require("fs");

let rawdata = fs.readFileSync("./TB Large/m24/delay.json");
let student = JSON.parse(rawdata);
var count = Object.keys(student).length;

var novo = [];
for (i = 0; i < count; i++) {
  var delay = student[i].target - student[i].origin;
  novo[i] = { delay: delay };
}

function chunkArray(myArray, chunk_size) {
  var index = 0;
  var arrayLength = myArray.length;
  var tempArray = [];

  for (index = 0; index < arrayLength; index += chunk_size) {
    var myChunk = myArray.slice(index, index + chunk_size);
    // Do something if you want with the group
    tempArray.push(myChunk);
  }

  return tempArray;
}
// Split in group of 3 items
var split = count / 31;
var result = chunkArray(novo, split);

// Outputs : [ [1,2,3] , [4,5,6] ,[7,8] ]

var means = [];
//console.log( result[0][0].delay);
for (var i = 1; i < 31; i++) {
  var tempMean = 0;
  for (var k = 0; k < split - 1; k++) {
    tempMean = tempMean + result[i][k].delay;
  }

  means[i] = tempMean / split;
}

var mean = 0;
for (var i = 1; i < 31; i++) {
  mean = mean + means[i];
}
mean = mean / 30;

var dptemp = 0;
for (var i = 1; i < 31; i++) {
  var store = (mean - means[i]) * (mean - means[i]);
  dptemp += store;
}

var dp = dptemp / 30;
var ie = mean - 1.96 * (dp / Math.sqrt(30));
console.log("Dividido em = " + split);
console.log("INTERVALO = " + ie);
console.log("Média DELAY = " + mean);
console.log("DP DELAY = " + dp);
console.log(
  "Mensagens= " +
    count +
    " totalizando " +
    count / 3600 +
    "mensagens por segundo"
);
var tempo1 = student[0].origin;
var tempo2 = student[count - 1].origin;
var tempo = tempo2 - tempo1;
console.log("Tempo de experimento= " + tempo);
/* RAM */
