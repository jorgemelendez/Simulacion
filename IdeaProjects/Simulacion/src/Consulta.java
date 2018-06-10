import java.util.LinkedList;

public class Consulta {
    private double tInicial;
    private double timeOut;
    private double tFinalReal;
    private TipoConsulta tipoConsulta;
    private int bloques;    //VER DONDE SE TIENE QUE CARGAR EL NUMERO DE BLOQUES
    private LinkedList<Evento> tiemposCola;//FALTA HACER LOS TIEMPOS EN COLA
    private LinkedList<Double> tiemposModulo;
    private EstadisticasSimulacion estadisticasSimulacion;

    public Consulta( double tInicial, double timeOut, TipoConsulta tipoConsulta, EstadisticasSimulacion estadisticasSimulacion){
        this.tInicial = tInicial;
        this.timeOut = timeOut;
        this.tipoConsulta = tipoConsulta;
        this.estadisticasSimulacion = estadisticasSimulacion;

        LinkedList<Evento> tiemposCola = new LinkedList<Evento>();
        LinkedList<Double> tiemposModulo = new LinkedList<Double>();
    }

    public void sumarTFinalReal( double tiempo ){

    }

    public double getTimeOut(){ return this.timeOut; }

    public int getBloques(){
        return this.bloques;
    }

    public TipoConsulta getTipoConsulta(){ return this.tipoConsulta; }

    public void setBloques( int bloques ){
        this.bloques = bloques;
    }

    public double getTFinalReal(){
        return this.tFinalReal;
    }

    public void setTFinalReal( double tiempo ){
        this.tFinalReal = tiempo;
    }

    /*El valor del parametro indiceModulo comienza en 1 para el primer modulo
    * llegada en true significa que el tiempo que enviaron es el tiempo de llegada a ese modulo
    * llegada en false significa que el tiempo que enviaron es el tiempo en el que esta saliendo el cliente de ese modulo
    * */
    public void setTiempoModulo(boolean llegada, int indiceModulo, double tiempo ){
        if( llegada ){
            tiemposModulo.add( indiceModulo, tiempo );
        }else {
            tiemposModulo.set( indiceModulo, tiempo - tiemposModulo.get( indiceModulo ) );
        }
    }

    public double getTiempoModulo(int indice ){
        return this.tiemposModulo.get( indice );
    }

    public boolean getReadOnly(){
        if( this.tipoConsulta == TipoConsulta.SELECT || this.tipoConsulta == TipoConsulta.JOIN ){
            return true;
        }
        return false;
    }

}
