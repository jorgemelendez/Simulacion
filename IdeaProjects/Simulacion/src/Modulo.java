import java.util.PriorityQueue;

public abstract class Modulo {
    protected int maxServers;
    protected int servidoresOcupados;
    protected int numeroModulo;
    protected GeneradorDeValoresAleatorios generadorDeValoresAleatorios;
    protected PriorityQueue<Consulta> cola;
    protected int largoCola;
    protected Modulo sigModulo;
    protected Modulo moduloConexion;
    protected EstadisticasSimulacion estadisticasSimulacion;
    protected PriorityQueue<Evento> listaEventos;

    public Modulo(/*Modulo sigModulo, *//*creo que hay que quitarlo porque es circular*/int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        /*this.sigModulo = sigModulo;*/
        this.maxServers = maxServers;
        this.estadisticasSimulacion = estadisticasSimulacion;
        this.listaEventos = listaEventos;
        this.servidoresOcupados = 0;
        this.generadorDeValoresAleatorios = new GeneradorDeValoresAleatorios();
        this.largoCola = 0;
    }

    public int getNumeroModulo() { return numeroModulo; }

    public void setSigModulo(Modulo sigModulo) { this.sigModulo = sigModulo; }

    public void setModuloConexion(Modulo moduloConexion) { this.moduloConexion = moduloConexion; }

    public int getLargoCola() { return largoCola; }

    /*VER SI ESTE METODO SE PUEDE HACER AQUI CON UN ATRIBUTO QUE DIGA CUAL MODULO ES EN EL QUE SE ESTA*/
    public abstract void generarLlegada( Consulta consulta, double tiempo );
    public abstract void procesarLlegada( Consulta consulta, double tiempo );
    /*VER SI ESTE METODO SE PUEDE HACER AQUI CON UN ATRIBUTO QUE DIGA CUAL MODULO ES EN EL QUE SE ESTA*/
    public abstract void generarSalida( Consulta consulta, double tiempo );
    public abstract void procesarSalida( Consulta consulta, double tiempo );
    public boolean matarConsultaTimeOutCola(Consulta consulta ){
        boolean borrado = this.cola.remove(consulta);
        if( borrado ) {
            largoCola--;
        }
        return borrado;
    }

}
