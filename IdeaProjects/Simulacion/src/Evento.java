public class Evento {
    private TipoEvento tipoEvento;
    private double tiempo;
    private Consulta consulta;

    public Evento( TipoEvento tipoEvento, double tiempo, Consulta consulta ){
        this.tipoEvento = tipoEvento;
        this.tiempo = tiempo;
        this.consulta = consulta;
    }
}
