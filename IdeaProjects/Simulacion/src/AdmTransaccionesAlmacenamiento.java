import java.util.PriorityQueue;

public class AdmTransaccionesAlmacenamiento extends Modulo {

    boolean ddlSiendoAtendido;

    public AdmTransaccionesAlmacenamiento(/*SubModuloEjecucion sigModulo, */int maxServers, EstadisticasSimulacion estadisticasSimulacion, PriorityQueue<Evento> listaEventos){
        super( /*sigModulo, */maxServers, estadisticasSimulacion, listaEventos);
        this.cola = new PriorityQueue<Consulta>( 11,new ComparadorColaAdmTransaccionesAlmacenamiento() );//ESTA ES LA DE PRIORIDAD DISTINTA
        this.ddlSiendoAtendido = false;
        this.numeroModulo = 4;
    }

    public void generarLlegada( Consulta consulta, double tiempo ){
        Evento nuevo = new Evento( TipoEvento.LlegadaAdmTransaccionesAlmacenamiento, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarLlegada( Consulta consulta, double tiempo ){
        consulta.setModuloActual( this );
        consulta.setBloques( ( !consulta.getReadOnly() )? 0
                                : (consulta.getTipoConsulta() == TipoConsulta.SELECT )? 1
                                : (int)this.generadorDeValoresAleatorios.generarValorUniforme(1,64) );
        if( consulta.getTipoConsulta() == TipoConsulta.DDL ){
            if( this.servidoresOcupados > 0 ){
                this.largoCola++;
                consulta.setTiempoCola(true,4,tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
                this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
            }else{
                this.ddlSiendoAtendido = true;//PONER EN FALSO CUANDO SALGA
                this.servidoresOcupados++;
                double tiempoSalida = tiempo +
                        (this.maxServers * 0.03) +
                        (consulta.getBloques() * 0.1);
                this.generarSalida(consulta, tiempoSalida);
                if ( tiempoSalida >= consulta.getTimeOut() ) {
                    //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                    this.moduloConexion.generarSalida( consulta,tiempoSalida );
                    consulta.setSalioTimeOut(true);
                } else {
                    this.sigModulo.generarLlegada(consulta, tiempoSalida);
                }
                consulta.setTiempoModulo(true, 4, tiempo);
                consulta.setTiempoCola(true,4,0);
            }
        }else {
            if ( !this.ddlSiendoAtendido && this.cola.isEmpty() && this.maxServers - this.servidoresOcupados > 0 ) {//tercer condicion es para cuando hay un ddl en cola y hay servers disponibles y llega una consulta que no es ddl
                this.servidoresOcupados++;
                double tiempoSalida = tiempo +
                        (this.maxServers * 0.03) +
                        (consulta.getBloques() * 0.1);
                this.generarSalida(consulta, tiempoSalida);
                if (tiempoSalida >= consulta.getTimeOut()) {
                    //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                    this.moduloConexion.generarSalida( consulta,tiempoSalida );
                    consulta.setSalioTimeOut(true);
                } else {
                    this.sigModulo.generarLlegada(consulta, tiempoSalida);
                }
                consulta.setTiempoModulo(true, 4, tiempo);
                consulta.setTiempoCola(true,4,0);
            } else {
                this.largoCola++;
                consulta.setTiempoCola(true, 4, tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
                this.cola.add(consulta);//VER COMO SE HACE LA COMPARACION INTERNA A LA COLA
            }
        }
    }

    public void generarSalida( Consulta consulta, double tiempo ){
        //consulta.setTFinalReal( tiempo );//CREO QUE NO ES NECESARIO ESTARLO ACTUALIZANDO
        Evento nuevo = new Evento( TipoEvento.SalidaAdmTransaccionesAlmacenamiento, tiempo, consulta );
        this.listaEventos.add( nuevo );
    }

    public void procesarSalida( Consulta consulta, double tiempo ){
        if( consulta.getTipoConsulta() == TipoConsulta.DDL ){
            this.ddlSiendoAtendido = false;
            this.servidoresOcupados--;
            if( this.largoCola > 0 ){
                while( this.largoCola != 0 && servidoresOcupados <= this.maxServers && this.cola.peek().getTipoConsulta() != TipoConsulta.DDL ){
                    this.largoCola--;
                    this.servidoresOcupados++;
                    Consulta nueva = this.cola.poll();
                    consulta.setTiempoCola(false, 4, tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
                    double tiempoSalidaNueva = tiempo +
                            (this.maxServers * 0.03) +
                            (consulta.getBloques() * 0.1);
                    this.generarSalida(nueva, tiempoSalidaNueva);
                    if (tiempoSalidaNueva >= consulta.getTimeOut()) {
                        //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                        this.moduloConexion.generarSalida( consulta,tiempoSalidaNueva );
                        consulta.setSalioTimeOut(true);
                    } else {
                        this.sigModulo.generarLlegada(consulta, tiempoSalidaNueva);
                    }
                    nueva.setTiempoModulo(true, 4, tiempo);//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
                }
            }else{
                this.servidoresOcupados--;
            }
        }else {
            if (this.largoCola > 0) {
                this.largoCola--;
                Consulta nueva = this.cola.poll();
                consulta.setTiempoCola(false, 4, tiempo);//debe de ir antes de agregarlo a cola para que el compareTo sepa el valor de entrada y ordene FIFO
                double tiempoSalidaNuevaConsulta = tiempo +
                        (this.maxServers * 0.03) +
                        (consulta.getBloques() * 0.1);
                this.generarSalida(nueva, tiempoSalidaNuevaConsulta);
                if (tiempoSalidaNuevaConsulta >= consulta.getTimeOut()) {
                    //VER COMO LE VAMOS A INFORMAR A ADMCLIENTES QUE SAQUE LA CONSULTAS
                    this.moduloConexion.generarSalida( consulta,tiempoSalidaNuevaConsulta );
                    consulta.setSalioTimeOut(true);
                } else {
                    this.sigModulo.generarLlegada(consulta, tiempoSalidaNuevaConsulta);
                }
                nueva.setTiempoModulo(true, 4, tiempo);//TIEMPO DE ENTRADA DE LA CONSULTA NUEVA
            } else {
                this.servidoresOcupados--;
            }
            consulta.setTiempoModulo(false, 4, tiempo - consulta.getTiempoModulo(4));
            //VER SI NO LE FALTA ALGO MAS
        }
    }

}
