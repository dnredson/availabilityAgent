"use strict";

const fs = require("fs");

let rawdata = fs.readFileSync("./TB Large/m32/monitor.json");
let student = JSON.parse(rawdata);

var count = Object.keys(student).length;
var max = student[1].cpu;
var novo2 = [];
for (i = 0; i < count; i++) {
  
var delay = student[i].cpu;
  
  novo2[i] = { delay: delay };

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
var split = count / 32;
var result = chunkArray(novo2, split);
console.log(result.length);
// Outputs : [ [1,2,3] , [4,5,6] ,[7,8] ]

var means = [];
//console.log( result[0][0].delay);
for (var i = 0; i < 32; i++) {
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
for (var i = 0; i < 31; i++) {
  var store = (mean - means[i]) * (mean - means[i]);
 

  dptemp += store;

}

var va = dptemp / 30;
var dp = Math.sqrt(va);
var ie = ( 1.96 * (dp / Math.sqrt(30)));
console.log("INTERVALO = " + ie);
console.log("Média CPU = " + mean);
console.log("dp"+dptemp);
console.log("Maximo:" + max/1024);
var ind = Math.round(split *2);
var tempo1 = student[ind].timestamp;
var tempo2 = student[count - 1].timestamp;
var tempo = tempo2 - tempo1;
console.log("Tempo de experimento= " + tempo);
/* RAM */
