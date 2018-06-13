import java.util.LinkedList;

public class Consulta implements Comparable<Consulta>{
    private double tInicial;
    private double timeOut;
    private boolean activa;
    private boolean salioTimeOut;
    private double tFinalReal;
    private TipoConsulta tipoConsulta;
    private int bloques;    //VER DONDE SE TIENE QUE CARGAR EL NUMERO DE BLOQUES
    private Modulo moduloActual; //comienza en 1, VER SI SE NECESITA, LO USO PARA VER CUAL CONSULTA VA PRIMERO QUE OTRA EN FIFO, Y CREO QUE NOS SIRVE PARA SABER DE CUAL COLA SACAR UNA CONSULTA
    private LinkedList<Double> tiemposCola;//FALTA HACER LOS TIEMPOS EN COLA
    private LinkedList<Double> tiemposModulo;
    private EstadisticasSimulacion estadisticasSimulacion;

    public Consulta( double tInicial, double timeOut, TipoConsulta tipoConsulta, EstadisticasSimulacion estadisticasSimulacion){
        this.tInicial = tInicial;
        this.timeOut = timeOut;
        this.activa = true;
        this.salioTimeOut = false;
        this.tipoConsulta = tipoConsulta;
        this.estadisticasSimulacion = estadisticasSimulacion;

        this.tiemposCola = new LinkedList<Double>();
        this.tiemposModulo = new LinkedList<Double>();
    }

    public boolean getSalioTimeOut() { return salioTimeOut; }

    public void setSalioTimeOut(boolean salioTimeOut) {
        this.salioTimeOut = salioTimeOut;
        //this.estadisticasSimulacion.addConsultaRechazado(this);//DESCOMENTAR SOLO SI EN LAS DESCARTADAS CUENTAN LAS TIMEOUT
    }

    public boolean isActiva() { return activa; }

    public void setActiva(boolean activa) { this.activa = activa; }

    public Modulo getModuloActual() { return moduloActual; }

    public int getNumeroModuloActual() { return moduloActual.getNumeroModulo(); }

    public void setModuloActual(Modulo moduloActual) { this.moduloActual = moduloActual; }

    public double getTimeOut(){ return this.timeOut; }

    public int getBloques(){ return this.bloques; }

    public TipoConsulta getTipoConsulta(){ return this.tipoConsulta; }

    public void setBloques( int bloques ){ this.bloques = bloques; }

    public double getTFinalReal(){ return this.tFinalReal; }

    public void setTFinalReal( double tiempo ){ this.tFinalReal = tiempo; }

    /*El valor del parametro indiceModulo comienza en 1 para el primer modulo
    * llegada en true significa que el tiempo que enviaron es el tiempo de llegada a ese modulo
    * llegada en false significa que el tiempo que enviaron es el tiempo en el que esta saliendo el cliente de ese modulo
    * */
    public void setTiempoModulo(boolean llegada, int indiceModulo, double tiempo ){
        indiceModulo--;
        if( llegada ){
            tiemposModulo.add( indiceModulo, tiempo );
        }else {
            tiemposModulo.set( indiceModulo, tiempo - tiemposModulo.get( indiceModulo ) );
        }
    }

    //El primer modulo es el 1, recuerde que el primer modulo no tiene cola, por lo que se le restan 2
    public void setTiempoCola(boolean llegada, int indiceModulo, double tiempo ){
        indiceModulo -= 2;
        if( llegada ){
            tiemposCola.add( indiceModulo, tiempo );
        }else {
            tiemposCola.set( indiceModulo, tiempo - tiemposCola.get( indiceModulo ) );
        }
    }

    public double getTiempoModulo(int indice ){ return this.tiemposModulo.get( indice-1 ); }

    public double getTiempoCola(int indice ){ return this.tiemposCola.get( indice-2 ); }

    public boolean getReadOnly(){
        if( this.tipoConsulta == TipoConsulta.SELECT || this.tipoConsulta == TipoConsulta.JOIN ){
            return true;
        }
        return false;
    }

    /*Estas comparaciones funcionan porque cuando se mete a cola, se debe poner el tiempo de entrada
    * y hasta que salga de cola se pone el tiempo que duro, y esta comparacion se debe de dar antes de que salga,
    * por lo que el tiempo que esta en el vector es el tiempo de entrada en cola*/
    @Override
    public int compareTo(Consulta otro ){
        if( this.getTiempoCola( this.getNumeroModuloActual() ) < otro.getTiempoCola( otro.getNumeroModuloActual() ) ){
            return -1;
        }else if( this.getTiempoCola( this.getNumeroModuloActual() ) > otro.getTiempoCola( otro.getNumeroModuloActual() ) ){
            return 1;
        }else{
            return 0;//creo que no se da porque solo llega una consulta en un solo instante
        }
    }

}
