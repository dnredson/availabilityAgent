module.exports.task = function (entity){
    
    var up;
    var available;
   /* ( async function test () {
        let services = [
            {
                address : 'www.uol.com.br'
                
            }
        ],
        status = await new ping ( services ).monitor ()
        up = status;
        console.log ( up.alive);
 
    }());
    */
    console.log(entity);


}


class ping {
    constructor( services){
        this.services = services
       
    }
    
    async monitor  () {
        
        let status = {
            url  : {},
            alias: {},
            alive:{}
        }
        for ( let service of this.services ) {
            let isAlive = await this.ping ( service )
            status.url  [ `${service.address}` ] = isAlive
            status.alive = isAlive
            
        }
        return status
    }
    ping ( connection ) {
        return new Promise ( ( resolve, reject )=>{
            const tcpp = require('tcp-ping');
            tcpp.ping( connection,( err, data)=> {
                let error = data.results [0].err            
                if ( !error ) {
                    resolve ( true )
                }
                if ( error ) {
                    resolve ( false )
                }
            });
        })        
    }
}
