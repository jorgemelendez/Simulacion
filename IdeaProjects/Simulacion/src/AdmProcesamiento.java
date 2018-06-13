import java.util.PriorityQueue;

public class AdmProcesamiento extends Modulo {

    public AdmProcesamiento(/*AdmTransaccionesAlmacenamiento sigModulo, */int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        super( /*sigModulo, */maxServers, estadisticasSimulacion, listaEventos);
        this.cola = new PriorityQueue<Consulta>();
        this.numeroModulo = 3;
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaAdmProcesamiento, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        consulta.setModuloActual( this );
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            double tiempoSalida = tiempo +
                                  ( (double)1 / 10 ) +
                                  ( this.generadorDeValoresAleatorios.generarValorUniforme(0,1) ) +
                                  ( this.generadorDeValoresAleatorios.generarValorUniforme(0,2) ) +
                                  ( this.generadorDeValoresAleatorios.generarValorExponencial(1/0.7) ) + //CREO QUE ES 1/0.7 PORQUE EL ENUNCIADO DICE QUE ES 0.7 EN PROMEDIO
                                  ( ( consulta.getReadOnly() )? 0.1 : 0.25 );
            this.generarSalida( consulta, tiempoSalida );
            if( tiempoSalida >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                this.moduloConexion.generarSalida( consulta,tiempoSalida );
                consulta.setSalioTimeOut(true);
            }else{
                this.sigModulo.generarLlegada(consulta, tiempoSalida);
            }
            consulta.setTiempoModulo(true, 3 , tiempo );
            consulta.setTiempoCola(true,3,0);
        }else{
            this.largoCola++;
            consulta.setTiempoCola(true,3,tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
            this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
        }
    }

    public void generarSalida( Consulta consulta, double tiempo ){
       // consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaAdmProcesamiento, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        if( this.largoCola > 0 ){
            this.largoCola--;
            Consulta nueva = this.cola.poll();
            consulta.setTiempoCola(false,3,tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
            double tiempoSalidaNuevaConsulta = tiempo +
                                         ( (double)1 / 10 ) +
                                         ( this.generadorDeValoresAleatorios.generarValorUniforme(0,1) ) +
                                         ( this.generadorDeValoresAleatorios.generarValorUniforme(0,2) ) +
                                         ( this.generadorDeValoresAleatorios.generarValorExponencial(1/0.7) ) + //CREO QUE ES 1/0.7 PORQUE EL ENUNCIADO DICE QUE ES 0.7 EN PROMEDIO
                                         ( ( consulta.getReadOnly() )? 0.1 : 0.25 );
            this.generarSalida( nueva, tiempoSalidaNuevaConsulta );
            if( tiempoSalidaNuevaConsulta >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                this.moduloConexion.generarSalida( consulta,tiempoSalidaNuevaConsulta );
                consulta.setSalioTimeOut(true);
            }else{
                this.sigModulo.generarLlegada(consulta, tiempoSalidaNuevaConsulta);
            }
            nueva.setTiempoModulo(true, 3 , tiempo );//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
        }else{
            this.servidoresOcupados--;
        }
        consulta.setTiempoModulo(false, 3 , tiempo - consulta.getTiempoModulo(3) );
        //VER SI NO LE FALTA ALGO MAS
    }

}
