public class Simulacion {
    private int k;
    private double t;
    private int n;
    private int p;
    private int m;
    private int cantEjec;
    private boolean rapido;
    private DBMS dbms;
    private Interfaz interfaz;
    private int tMaxSim;
    private EstadisticasSimulacion estadisticas;

    public void ejecutarSimulaciones(){
        //Este es el ciclo de todas las simulaciones
        for( int i = 0; i < this.cantEjec; i++ ){
            this.dbms = new DBMS( this.k, this.t, this.n, this.p, this.m, this.tMaxSim, this.estadisticas, this.interfaz );
            this.dbms.ejecutarSimulacion();
        }
    }

    public void pedirDatos(){
        //Solicitar datos
        //interfaz.pedirParametros();//VER COMO TRAER LOS DATOS, DESCOMENTAR
        this.k = 1;
        this.t = 15;
        this.n = 1;
        this.p = 1;
        this.m = 1;
        this.cantEjec = 1;
        this.rapido = false;
        this.tMaxSim = 500;
        this.estadisticas = new EstadisticasSimulacion();
        this.interfaz = new Interfaz();//QUITAR
    }

    public void generarEstadisticasGenerales(){
        //Aqui es donde se sacan las estadisticas generales para mostrarlas a al usuario en la interfaz luego
    }

    public static void main(String [] args){
        Simulacion simulacion = new Simulacion();
        simulacion.pedirDatos();
        simulacion.ejecutarSimulaciones();
        System.out.println("Hola mundo");
    }
}
