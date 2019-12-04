'use strict';

const fs = require('fs');

let rawdata = fs.readFileSync('largemonitor.json');
let student = JSON.parse(rawdata);

var count = Object.keys(student).length;

    function chunkArray(myArray, chunk_size){
        var index = 0;
        var arrayLength = myArray.length;
        var tempArray = [];
        
        for (index = 0; index < arrayLength; index += chunk_size) {
            var myChunk = myArray.slice(index, index+chunk_size);
            // Do something if you want with the group
            tempArray.push(myChunk);
        }
    
        return tempArray;
    }
    // Split in group of 3 items
    var split = count/31 ;
    var result = chunkArray(student, split);
    // Outputs : [ [1,2,3] , [4,5,6] ,[7,8] ]
    var means=[];
    
       
    for (var i=1; i< 30; i++){
        var tempMean = 0;
    for ( var k = 0; k < split; k++){
        console.log("K"+k+"i"+i);
        //tempMean = tempMean + result[i][k].orioncpu;
    }
    
    //means[i]=tempMean/split;

}
/*
var mean =0;
for (var i = 1; i < 30; i++){

mean = mean + means[i];
}
mean = mean/30;

var dptemp=0;
for (var i=1; i< 31; i++){
  
  var  store = ((mean - means[i]) * (mean - means[i]));
  dptemp += store;
   
}
var dp = dptemp/30;

var ie = mean - (1.96*dp);

console.log("INTERVALO = "+ ie);
console.log("Média CPU = "+mean);
console.log("DP CPU = "+dp);

var means=[];
    console.log(result[0][0].orionmem);

    for (var i=1; i< 31; i++){
    var tempMean = 0;
for ( var k = 0; k < split; k++){
    tempMean = tempMean + result[i][k].orionmem;
}
means[i]=tempMean/split;

}

var mean =0;
for (var i = 1; i < 31; i++){

mean = mean + means[i];
}
mean = mean/30;

var dptemp=0;
for (var i=1; i< 31; i++){

var  store = ((mean - means[i]) * (mean - means[i]));
dptemp += store;

}
var dp = dptemp/30;

var ie = mean - (1.96*dp);

console.log("INTERVALO RAM = "+ ie);
console.log("Média RAM = "+mean);
console.log("DP RAM = "+dp);
*/