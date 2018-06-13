import java.util.PriorityQueue;

public class DBMS {
    /*private int k;
    private double t;
    private int n;
    private int p;
    private int m;*/
    private int tMaxSim;
    private EstadisticasSimulacion estadisticasSimulacion;
    private Interfaz interfaz;
    private  PriorityQueue<Evento> listaEventos;
    private GeneradorDeValoresAleatorios generadorDeValoresAleatorios;
    private double reloj;
    private AdmClientes admClientes;
    private AdmProcesos admProcesos;
    private AdmProcesamiento admProcesamiento;
    private AdmTransaccionesAlmacenamiento admTransaccionesAlmacenamiento;
    private SubModuloEjecucion subModuloEjecucion;

    public DBMS( int k, double t, int n, int p, int m, int tMaxSim, EstadisticasSimulacion estadisticasSimulacion, Interfaz interfaz){
        /*this.k = k;
        this.t = t;
        this.n = n;
        this.p = p;
        this.m = m;*/
        this.tMaxSim = tMaxSim;
        this.estadisticasSimulacion = estadisticasSimulacion;
        this.interfaz = interfaz;
        this.listaEventos = new PriorityQueue<Evento>();
        this.generadorDeValoresAleatorios = new GeneradorDeValoresAleatorios();
        this.reloj = 0;


        //Esto talvez se pueda pasar a un metodo aparte, pero como son cosas del DBMS se pueden dejar aqui REVISAR
        //Ojo que si se pasa a un metodo si se necesitan los atributos como de la clase
        this.admClientes = new AdmClientes( /*this.*/k, this.estadisticasSimulacion, this.listaEventos,/*this.*/t );
        this.admProcesos = new AdmProcesos(1, this.estadisticasSimulacion, this.listaEventos );
        this.admProcesamiento = new AdmProcesamiento( /*this.*/n, this.estadisticasSimulacion, this.listaEventos );
        this.admTransaccionesAlmacenamiento = new AdmTransaccionesAlmacenamiento( /*this.*/p,this.estadisticasSimulacion, this.listaEventos);
        this.subModuloEjecucion = new SubModuloEjecucion( /*this.*/m, this.estadisticasSimulacion, this.listaEventos );
        //Hacer todas las cargas de siguietes modulos ahora que los tiene creados
        this.admClientes.setSigModulo( this.admProcesos );
        this.admProcesos.setSigModulo( this.admProcesamiento );
        this.admProcesamiento.setSigModulo( this.admTransaccionesAlmacenamiento );
        this.admTransaccionesAlmacenamiento.setSigModulo( this.subModuloEjecucion );
        this.subModuloEjecucion.setSigModulo( this.admClientes );
        //Hacer todas las cargas de admcliente en los otros modulos para que puedan generar la salida de timeout
        this.admProcesos.setModuloConexion( this.admClientes );
        this.admProcesamiento.setModuloConexion( this.admClientes );
        this.admTransaccionesAlmacenamiento.setModuloConexion( this.admClientes );
        this.subModuloEjecucion.setModuloConexion( this.admClientes );

        TipoConsulta tipoConsulta = this.generadorDeValoresAleatorios.generarValorTipoConsulta();
        Consulta nuevaConsulta = new Consulta( 0, t, tipoConsulta, this.estadisticasSimulacion );
        this.admClientes.generarLlegada( nuevaConsulta, 0 );
    }

    private void procesarEvento( Evento evento ){
        System.out.println( "Evento: "+ evento.getTipoEvento().name()+ "\n"+
                            "Consulta: "+evento.getConsulta().getTipoConsulta().name()+"\n"+
                            "Tiempo: "+ evento.getTiempo()+ "\n\n" );
        switch( evento.getTipoEvento() ){
            case LlegadaAdmCliente:
                this.admClientes.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
                break;
            case LlegadaAdmProcesos:
                this.admProcesos.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
                break;
            case LlegadaAdmProcesamiento:
                this.admProcesamiento.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
                break;
            case LlegadaAdmTransaccionesAlmacenamiento:
                this.admTransaccionesAlmacenamiento.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
                break;
            case LlegadaSubModuloEjecucion:
                this.subModuloEjecucion.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaAdmCliente:
                this.admClientes.procesarSalida( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaAdmProcesos:
                this.admProcesos.procesarSalida( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaAdmProcesamiento:
                this.admProcesamiento.procesarSalida( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaAdmTransaccionesAlmacenamiento:
                this.admTransaccionesAlmacenamiento.procesarSalida( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaSubModuloEjecucion:
                this.subModuloEjecucion.procesarSalida( evento.getConsulta(), evento.getTiempo() );
                break;
            case SalidaTimeOut:
                this.admClientes.procesarSalidaTimeOut( evento.getConsulta(), evento.getTiempo() );
                break;
        }
    }

    public void ejecutarSimulacion(){
        do {
            Evento nuevo = this.listaEventos.poll();
            this.reloj = nuevo.getTiempo();
            System.out.println( "Reloj: " + this.reloj + "" );
            System.out.println( "Largo cola AdmClientes: " + this.admClientes.getLargoCola() + "\n" +
                                "Largo cola AdmProcesos: " + this.admProcesos.getLargoCola() + "\n" +
                                "Largo cola AdmProcesamiento: " + this.admProcesamiento.getLargoCola() + "\n" +
                                "Largo cola AdmTransacciones: " + this.admTransaccionesAlmacenamiento.getLargoCola() + "\n" +
                                "Largo cola AdmSubEjecucion: " + this.subModuloEjecucion.getLargoCola() + "" );
            System.out.println( "Conexiones descartadas: " + this.estadisticasSimulacion.getConsultasRechazadas() );
            this.procesarEvento(nuevo);
        } while( !this.listaEventos.isEmpty() && this.listaEventos.peek().getTiempo() < this.tMaxSim/*this.reloj < this.tMaxSim*/ );
    }

}
