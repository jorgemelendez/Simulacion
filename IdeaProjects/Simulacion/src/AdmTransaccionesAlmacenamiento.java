import java.util.PriorityQueue;

public class AdmTransaccionesAlmacenamiento extends Modulo {

    public AdmTransaccionesAlmacenamiento(SubModuloEjecucion sigModulo, int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        super( sigModulo, maxServers, estadisticasSimulacion, listaEventos);
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaModTracsac, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            double tiempoSalida = tiempo +
                                  ( this.maxServers * 0.03 ) +
                                  ( consulta.getBloques() * 0.1 );
            this.generarSalida( consulta, tiempoSalida );
            if( tiempoSalida >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
            }else{
                this.sigModulo.generarLlegada(consulta, tiempo);
            }
            consulta.setTiempoModulo(true, 4 , tiempo );
        }else{
            this.largoCola++;
            this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
        }
    }

    public void generarSalida( Consulta consulta, double tiempo ){
        consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaModTracsac, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        if( this.largoCola > 0 ){
            this.largoCola--;
            Consulta nueva = this.cola.poll();
            double tiempoSalidaNuevaConsulta = tiempo +
                                         ( this.maxServers * 0.03 ) +
                                         ( consulta.getBloques() * 0.1 );
            this.generarSalida( nueva, tiempoSalidaNuevaConsulta );
            if( tiempoSalidaNuevaConsulta >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
            }else{
                this.sigModulo.generarLlegada(consulta, tiempo);
            }
            nueva.setTiempoModulo(true, 4 , tiempo );//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
        }else{
            this.servidoresOcupados--;
        }
        consulta.setTiempoModulo(false, 4 , tiempo - consulta.getTiempoModulo(4) );
        //VER SI NO LE FALTA ALGO MAS
    }

    public void matarTimeOut( double tiempo ){
        //VER COMO LO PENSO ARAYA, CREO QUE ES VERIFICANDO EN GENERAR SALIDA QUE LE TIEMPO GENERADO NO SEA MAYOR QUE EL TIME OUT
        // OJO QUE SI SE HACE TIME OUT DEBO PONER ESE TIEMPO COMO TFinalReal DE LA CONSULTA
    }

}
