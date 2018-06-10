import java.util.PriorityQueue;

public abstract class Modulo {
    protected int maxServers;
    protected int servidoresOcupados;
    protected ValoresAleatorios valoresAleatorios;
    protected PriorityQueue<Consulta> cola;
    protected int largoCola;
    protected Modulo sigModulo;
    protected EstadisticasSimulacion estadisticasSimulacion;
    protected PriorityQueue<Evento> listaEventos;

    public Modulo(Modulo sigModulo, int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        this.sigModulo = sigModulo;
        this.maxServers = maxServers;
        this.estadisticasSimulacion = estadisticasSimulacion;
        this.listaEventos = listaEventos;
        this.servidoresOcupados = 0;
        this.valoresAleatorios = new ValoresAleatorios();
        this.cola = new PriorityQueue<Consulta>();
        this.largoCola = 0;
    }

    /*VER SI ESTE METODO SE PUEDE HACER AQUI CON UN ATRIBUTO QUE DIGA CUAL MODULO ES EN EL QUE SE ESTA*/
    public abstract void generarLlegada( Consulta consulta, double tiempo );
    public abstract void procesarLlegada( Consulta consulta, double tiempo );
    /*VER SI ESTE METODO SE PUEDE HACER AQUI CON UN ATRIBUTO QUE DIGA CUAL MODULO ES EN EL QUE SE ESTA*/
    public abstract void generarSalida( Consulta consulta, double tiempo );
    public abstract void procesarSalida( Consulta consulta, double tiempo );
    public abstract void matarTimeOut( double tiempo );

}
