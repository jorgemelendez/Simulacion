import java.util.PriorityQueue;

public class AdmClientes extends Modulo{

    private double tMax;

    public AdmClientes(/*AdmProcesos sigModulo, */int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos, double tMax){
        super( /*sigModulo, */maxServers, estadisticasSimulacion, listaEventos);
        this.tMax = tMax;
        this.numeroModulo = 1;
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaAdmCliente, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        consulta.setModuloActual( this );
        if( this.maxServers - this.servidoresOcupados > 0 ){ //Hay servidores libres?
            this.servidoresOcupados++;
            this.sigModulo.generarLlegada( consulta, tiempo );
            consulta.setTiempoModulo(true, 1 , tiempo );
            this.generarSalidaTimeOut(consulta,tiempo+this.tMax);
        }else{
            this.estadisticasSimulacion.addConsultaRechazado( consulta ); //VER SI LO METEMOS EN CLIENTES AUNQUE
        }
        double tInicial = tiempo + this.generadorDeValoresAleatorios.generarValorExponencial(30/(double)60);
        double timeOut =  tInicial + this.tMax;
        TipoConsulta tipoConsulta = this.generadorDeValoresAleatorios.generarValorTipoConsulta();
        Consulta nuevaConsulta = new Consulta( tInicial, timeOut, tipoConsulta, this.estadisticasSimulacion );
        this.generarLlegada( nuevaConsulta, tInicial );
    }

    /*el tiempo debe ser el tiempo actual del reloj +  b/64*/
    public void generarSalida( Consulta consulta, double tiempo ){
        consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO en los otros modulos
        Evento nuevo = new Evento( TipoEvento.SalidaAdmCliente, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        this.servidoresOcupados--;
        consulta.setTiempoModulo(false, 1 , tiempo - consulta.getTiempoModulo(1) );
        consulta.setActiva(false);//Indica que la consulta ya salió
        //VER SI NO LE FALTA ALGO MAS
    }

    public void generarSalidaTimeOut( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.SalidaTimeOut, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalidaTimeOut( Consulta consulta, double tiempo ){//VER SI ESTAS CONSULTAS SE METEN A ESTADISTICAS
        //System.out.println("EventoTimeOut");
        if( consulta.isActiva() ){
            System.out.println(consulta.getModuloActual().getNumeroModulo());
            boolean borradoDeCola = consulta.getModuloActual().matarConsultaTimeOutCola( consulta ); //Se puede hacer porque siempre va a estar en un modulo que no es el primero, pues apenas llega al primero pasa al sig
            if( borradoDeCola ){
                this.generarSalida( consulta, tiempo );
                consulta.setSalioTimeOut(true);
            }else{
                //Ignorar porque no se deja pasar del modulo en el que esta, y como se da cuenta que no sigue entonces se genera la salida del modulo admclientes
            }
        }else{
            //Ignorar porque ya salio
        }
    }

}
