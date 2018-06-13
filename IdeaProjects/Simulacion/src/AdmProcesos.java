import java.util.PriorityQueue;

public class AdmProcesos extends Modulo{

    public AdmProcesos(/*AdmProcesamiento sigModulo, */int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        super( /*sigModulo, */maxServers, estadisticasSimulacion, listaEventos);
        this.cola = new PriorityQueue<Consulta>();
        this.numeroModulo = 2;
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaAdmProcesos, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        consulta.setModuloActual( this );
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            double tiempoSalida = tiempo + this.generadorDeValoresAleatorios.generarValorNormal(1.0,0.01);
            this.generarSalida( consulta, tiempoSalida );
            if( tiempoSalida >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                this.moduloConexion.generarSalida( consulta,tiempoSalida );
                consulta.setSalioTimeOut(true);
            }else{
                this.sigModulo.generarLlegada( consulta, tiempoSalida );
            }
            consulta.setTiempoModulo(true, 2 , tiempo );
            consulta.setTiempoCola(true,2,0);
        }else{
            this.largoCola++;
            consulta.setTiempoCola(true,2,tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
            this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
        }
    }

    public void generarSalida( Consulta consulta, double tiempo ){
        //consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaAdmProcesos, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        if( this.largoCola > 0 ){
            this.largoCola--;
            Consulta nueva = this.cola.poll();
            nueva.setTiempoCola(false,2,tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
            double tiempoSalidaNuevaConsulta = tiempo + this.generadorDeValoresAleatorios.generarValorNormal(1.0,0.01);
            this.generarSalida( nueva, tiempoSalidaNuevaConsulta );
            if( tiempoSalidaNuevaConsulta >= consulta.getTimeOut()){
                //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                this.moduloConexion.generarSalida( consulta,tiempoSalidaNuevaConsulta );
                consulta.setSalioTimeOut(true);
            }else{
                this.sigModulo.generarLlegada(consulta, tiempoSalidaNuevaConsulta);
            }
            nueva.setTiempoModulo(true, 2 , tiempo );//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
        }else{
            this.servidoresOcupados--;
        }
        consulta.setTiempoModulo(false, 2 , tiempo - consulta.getTiempoModulo(2) );
        //VER SI NO LE FALTA ALGO MAS
    }
    
}
