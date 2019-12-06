let promise1 = new Promise((resolve,reject)=>{
setTimeout(()=>{
resolve ('A promise 1 foi resolvida');

},2000);

});


let promise2 = new Promise((resolve,reject)=>{
    setTimeout(()=>{
    resolve ('A promise 2 foi resolvida');
    
    },2000);
    
    });

Promise.all([promise1,promise2])
.then(([resultado1,resultado2])=>{
console.log("Promises resoltidas");
console.log(resultado1);
console.log(resultado2);

})
.catch((err)=>{
console.log("Uma promise foi rejeitada");
console.log(err);

})


// promise2.then((resultado)=>{

//     console.log(resultado);
// }).catch((err) =>{
//     console.error(err)
// });