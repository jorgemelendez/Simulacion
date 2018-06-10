import java.util.LinkedList;

public class EstadisticasSimulacion {
    private LinkedList< LinkedList<Consulta> > consultas;
    private int consultasRechazadas;

    public void addConsultaFinalizado( Consulta consulta ){

    }

    public void addConsultaRechazado( Consulta consulta ){

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
