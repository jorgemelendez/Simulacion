import java.util.PriorityQueue;

public class DBMS {
    private int k;
    private double t;
    private int n;
    private int p;
    private int m;
    private int tMaxSim;
    private EstadisticasSimulacion estadisticasSimulacion;
    private Interfaz interfez;
    private  PriorityQueue<Evento> listaEventos;
    private ValoresAleatorios tipoConsulta;
    private double reloj;
    private AdmClientes admClientes;
    private AdmProcesos admProcesos;
    private AdmProcesamiento admProcesamiento;
    private AdmTransaccionesAlmacenamiento admTransaccionesAlmacenamiento;
    private SubModuloEjecucion subModuloEjecucion;

    public DBMS( int k, double t, int n, int p, int m, int tMaxSim, EstadisticasSimulacion estadisticasSimulacion){
        this.k = k;
        this.t = t;
        this.n = n;
        this.p = p;
        this.m = m;
        this.tMaxSim = tMaxSim;
        this.estadisticasSimulacion = estadisticasSimulacion;
    }

    private void ejecutarSimulacion(){

    }

    private void matarTimeOut(){

    }
}
