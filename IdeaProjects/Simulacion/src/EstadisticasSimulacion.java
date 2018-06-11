import java.util.LinkedList;

public class EstadisticasSimulacion {
    private LinkedList< LinkedList<Consulta> > consultas;
    private int consultasRechazadas;

    public EstadisticasSimulacion(){
        this.consultasRechazadas = 0;
    }

    public void addConsultaFinalizado( Consulta consulta ){

    }

    public int getConsultasRechazadas() { return consultasRechazadas; }

    public void addConsultaRechazado(Consulta consulta ){
        this.consultasRechazadas++;
    }

    /*OJO CUANDO SAQUE TIEMPOS QUE EL TIEMPO DE PROCESAMIENTO Y EJECUCION DEBEN SER UNO SOLO, HAY SUMARLOS*/
    public LinkedList<Integer> getTamannoPromedioColas(){
        return new LinkedList<Integer>();
    }

    /*OJO CUANDO SAQUE TIEMPOS QUE EL TIEMPO DE PROCESAMIENTO Y EJECUCION DEBEN SER UNO SOLO, HAY SUMARLOS*/
    public LinkedList<Double> getTamannoPromedioDeSentenciaPorModulo(){
        return new LinkedList<Double>();
    }
}
