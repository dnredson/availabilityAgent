class Spacecraft {
    constructor (public propulsor:string){
    
    }
    
    jumpIntoHyperspace(){
        console.log(`Entering hyperspace with ${this.propulsor}`)
    }
    
    }


    
 interface Containership{

    cargoContainers?: number
}

export {Spacecraft,Containership}
