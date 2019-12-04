import {Spacecraft,Containership}from './base-ships'
import {MilleniumFalcon}from './startfighters'

let falcon = new MilleniumFalcon()
falcon.jumpIntoHyperspace()


let goodForTheJob = (ship: Containership) => ship.cargoContainers >2

console.log(`Is falcon good for the job? ${goodForTheJob(falcon)? 'Yes':'No'}`)