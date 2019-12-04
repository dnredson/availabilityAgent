"use strict";
exports.__esModule = true;
var startfighters_1 = require("./startfighters");
var falcon = new startfighters_1.MilleniumFalcon();
falcon.jumpIntoHyperspace();
var goodForTheJob = function (ship) { return ship.cargoContainers > 2; };
console.log("Is falcon good for the job? " + (goodForTheJob(falcon) ? 'Yes' : 'No'));
