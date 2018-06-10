import java.util.PriorityQueue;

public class SubModuloEjecucion extends Modulo{

    public SubModuloEjecucion(AdmClientes sigModulo, int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        super( sigModulo, maxServers, estadisticasSimulacion, listaEventos);
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaModEjecucion, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            double tiempoSalida = tiempo +
                                  ( ( consulta.getReadOnly() )?
                                          ( (consulta.getBloques() * consulta.getBloques())/(double)1000 )//VER SI ES ENTRE 1000 POR SER MILISEGUNDO
                                    :
                                          ( ( consulta.getTipoConsulta() == TipoConsulta.DDL )? 0.5 : 1) );
            this.generarSalida( consulta, tiempoSalida );

            double tiempoSalidaSigModulo = tiempoSalida + consulta.getBloques()/(double)64 ;
            if( tiempoSalidaSigModulo >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
            }else{
                this.sigModulo.generarLlegada(consulta, tiempoSalidaSigModulo);
            }
            consulta.setTiempoModulo(true, 5 , tiempo );
        }else{
            this.largoCola++;
            this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
        }
    }

    public void generarSalida( Consulta consulta, double tiempo ){
        consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaModEjecucion, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    /*HAY QUE VER QUE PASO CON LOS CASOS DEL DLL, HAY QUE INCLUIR CIERTAS CONDICIONES AQUI*/
    public void procesarSalida( Consulta consulta, double tiempo ){
        if( this.largoCola > 0 ){
            this.largoCola--;
            Consulta nueva = this.cola.poll();
            double tiempoSalidaNuevaConsulta = tiempo +
                                               ( ( consulta.getReadOnly() )?
                                                        ( (consulta.getBloques() * consulta.getBloques())/(double)1000 )//VER SI ES ENTRE 1000 POR SER MILISEGUNDO
                                                  :
                                                        ( ( consulta.getTipoConsulta() == TipoConsulta.DDL )? 0.5 : 1) );
            this.generarSalida( nueva, tiempoSalidaNuevaConsulta );

            double tiempoSalidaSigModulo = tiempoSalidaNuevaConsulta + consulta.getBloques()/(double)64 ;
            if( tiempoSalidaSigModulo >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
            }else{
                this.sigModulo.generarLlegada(consulta, tiempoSalidaSigModulo);
            }
            nueva.setTiempoModulo(true, 5 , tiempo );//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
        }else{
            this.servidoresOcupados--;
        }
        consulta.setTiempoModulo(false, 5 , tiempo - consulta.getTiempoModulo(5) );
        //VER SI NO LE FALTA ALGO MAS
    }

    public void matarTimeOut( double tiempo ){
        //VER COMO LO PENSO ARAYA, CREO QUE ES VERIFICANDO EN GENERAR SALIDA QUE LE TIEMPO GENERADO NO SEA MAYOR QUE EL TIME OUT
        // OJO QUE SI SE HACE TIME OUT DEBO PONER ESE TIEMPO COMO TFinalReal DE LA CONSULTA
    }

}
