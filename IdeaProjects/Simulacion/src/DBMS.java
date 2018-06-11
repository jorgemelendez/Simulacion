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

        TipoConsulta tipoConsulta = this.generadorDeValoresAleatorios.generarValorTipoConsulta();
        this.admClientes.generarLlegada( new Consulta( 0, t, tipoConsulta, this.estadisticasSimulacion ), 0 );
    }

    private void procesarEvento( Evento evento ){
        System.out.println( "Evento: "+ evento.getTipoEvento().name()+ "\n"+
                            "Consulta: "+evento.getConsulta().getTipoConsulta().name()+"\n"+
                            "Tiempo: "+ evento.getTiempo()+ "\n\n" );
        if( evento.getTipoEvento() == TipoEvento.LlegadaAdmCliente ){
            this.admClientes.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.LlegadaAdmProcesos ){
            this.admProcesos.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.LlegadaAdmProcesamiento ){
            this.admProcesamiento.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.LlegadaAdmTransaccionesAlmacenamiento ){
            this.admTransaccionesAlmacenamiento.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.LlegadaSubModuloEjecucion ){
            this.subModuloEjecucion.procesarLlegada( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.SalidaAdmCliente ){
            this.admClientes.procesarSalida( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.SalidaAdmProcesos ){
            this.admProcesos.procesarSalida( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.SalidaAdmProcesamiento ){
            this.admProcesamiento.procesarSalida( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.SalidaAdmTransaccionesAlmacenamiento ){
            this.admTransaccionesAlmacenamiento.procesarSalida( evento.getConsulta(), evento.getTiempo() );
        }else if( evento.getTipoEvento() == TipoEvento.SalidaSubModuloEjecucion ){
            this.subModuloEjecucion.procesarSalida( evento.getConsulta(), evento.getTiempo() );
        }
    }

    public void ejecutarSimulacion(){
        while( !this.listaEventos.isEmpty() && this.reloj < this.tMaxSim ) {
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
        }
    }

    private void matarTimeOut(){

    }
}
