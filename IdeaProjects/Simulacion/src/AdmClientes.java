import java.util.PriorityQueue;

public class AdmClientes extends Modulo{

    private double tMax;

    public AdmClientes(/*AdmProcesos sigModulo, */int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos, double tMax){
        super( /*sigModulo, */maxServers, estadisticasSimulacion, listaEventos);
        this.tMax = tMax;
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaAdmCliente, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        consulta.setModuloActual( 1 );
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            this.sigModulo.generarLlegada( consulta, tiempo );
            consulta.setTiempoModulo(true, 1 , tiempo );
        }else{
            this.estadisticasSimulacion.addConsultaRechazado( consulta ); //VER SI LO METEMOS EN CLIENTES AUNQUE
        }
        double tInicial = tiempo + this.generadorDeValoresAleatorios.generarValorExponencial(30/(double)60);
        double timeOut =  tInicial + this.tMax;
        TipoConsulta tipoConsulta = this.generadorDeValoresAleatorios.generarValorTipoConsulta();
        this.generarLlegada( new Consulta( tInicial, timeOut, tipoConsulta, this.estadisticasSimulacion ), tInicial );
    }

    /*el tiempo debe ser el tiempo actual del reloj +  b/64*/
    public void generarSalida( Consulta consulta, double tiempo ){
        consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaAdmCliente, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        this.servidoresOcupados--;
        consulta.setTiempoModulo(false, 1 , tiempo - consulta.getTiempoModulo(1) );
        //VER SI NO LE FALTA ALGO MAS
    }

    public void matarTimeOut( double tiempo ){
        //VER COMO LO PENSO ARAYA, CREO QUE ES VERIFICANDO EN GENERAR SALIDA QUE LE TIEMPO GENERADO NO SEA MAYOR QUE EL TIME OUT
        // OJO QUE SI SE HACE TIME OUT DEBO PONER ESE TIEMPO COMO TFinalReal DE LA CONSULTA
    }

}
