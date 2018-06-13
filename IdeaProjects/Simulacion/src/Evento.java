public class Evento implements Comparable<Evento>{
    private TipoEvento tipoEvento;
    private double tiempo;
    private Consulta consulta;

    public Evento( TipoEvento tipoEvento, double tiempo, Consulta consulta ){
        this.tipoEvento = tipoEvento;
        this.tiempo = tiempo;
        this.consulta = consulta;
    }

    public double getTiempo() { return tiempo; }

    public Consulta getConsulta() { return consulta; }

    public TipoEvento getTipoEvento() { return tipoEvento; }

    /*Este comparador es el que utiliza PriorityQueue como comparador natural, este para la lista de eventos*/
    @Override
    public int compareTo(Evento otro ){
        if( this.tiempo < otro.getTiempo() ){
            //System.out.println("Sali ayi con"+this.getTipoEvento().name());
            return -1;
        }else if( this.tiempo > otro.getTiempo() ){
            return 1;
        }
        /*if( this.tipoEvento.name().charAt(0) == 'S' ){//Creo que aqui pasa siempre las salidas de primero, no importa si se genera la salida despues de haber ejecutado una, porque este la va a meter en la cabeza de la cola
            return -1;
        }else if( otro.getTipoEvento().name().charAt(0) == 'S' ){
            return 1;
        }*/
        if( this.tipoEvento == TipoEvento.SalidaSubModuloEjecucion ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaSubModuloEjecucion ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.SalidaAdmProcesos ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaAdmProcesos ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.SalidaAdmProcesamiento ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaAdmProcesamiento ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.SalidaAdmTransaccionesAlmacenamiento ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaAdmTransaccionesAlmacenamiento ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.SalidaAdmCliente ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaAdmCliente ){//Este va de ultimo porque en caso de timeout se necesita que salga de los otros primero
            return 1;
        }else if( this.tipoEvento == TipoEvento.SalidaTimeOut ){//Este va despues de las salidas por si en el mismo tiempo hay time out pero tambien salia normal, entonces para que salga normal y no por time out
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.SalidaTimeOut ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.LlegadaSubModuloEjecucion ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.LlegadaSubModuloEjecucion ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.LlegadaAdmCliente ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.LlegadaAdmCliente ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.LlegadaAdmProcesos ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.LlegadaAdmProcesos ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.LlegadaAdmProcesamiento ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.LlegadaAdmProcesamiento ){
            return 1;
        }else if( this.tipoEvento == TipoEvento.LlegadaAdmTransaccionesAlmacenamiento ){
            return -1;
        }else if( otro.getTipoEvento() == TipoEvento.LlegadaAdmTransaccionesAlmacenamiento ){
            return 1;
        }
        return 0;//Este es el caso donde ambos son llegadas, no importa cual se ejecuta primero porque todas tienen el mismo tiempo
    }
}
