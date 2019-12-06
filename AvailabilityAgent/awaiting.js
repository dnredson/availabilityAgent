async function main(){

   
getResultado(5).then((resultado) =>{
    console.log("o valor é maior que 10");
}).then(getResultado(3).then((resultado)=>{
    console.log("o valor é menor que 10");
}))

}

var main = await main();
console.log ("Main executada!");
function getResultado(parametro){

    return new Promise((resolve,reject)=>{
setTimeout(()=>{
resolve(parametro*2.5);
},3000);

});

    }
