import java.util.Comparator;

public class ComparadorColaAdmTransaccionesAlmacenamiento implements Comparator<Consulta> {

    public int compare(Consulta c1, Consulta c2){
        //DLL
        if( c1.getTipoConsulta() == TipoConsulta.DDL && c1.getTipoConsulta() == c2.getTipoConsulta() ){
            return 0;
        }
        if( c1.getTipoConsulta() == TipoConsulta.DDL ){
            return -1;
        }else if( c2.getTipoConsulta() == TipoConsulta.DDL ) {
            return 1;
        }

        //UPDATE
        if( c1.getTipoConsulta() == TipoConsulta.UPDATE && c1.getTipoConsulta() == c2.getTipoConsulta() ){
            return 0;
        }
        if( c1.getTipoConsulta() == TipoConsulta.UPDATE ){
            return -1;
        }else if( c2.getTipoConsulta() == TipoConsulta.UPDATE ) {
            return 1;
        }

        //JOIN
        if( c1.getTipoConsulta() == TipoConsulta.JOIN && c1.getTipoConsulta() == c2.getTipoConsulta() ){
            return 0;
        }
        if( c1.getTipoConsulta() == TipoConsulta.JOIN ){
            return -1;
        }else if( c2.getTipoConsulta() == TipoConsulta.JOIN ) {
            return 1;
        }

        //SELECT
        if( c1.getTipoConsulta() == TipoConsulta.SELECT && c1.getTipoConsulta() == c2.getTipoConsulta() ){
            return 0;
        }
        if( c1.getTipoConsulta() == TipoConsulta.SELECT ){
            return -1;
        }else if( c2.getTipoConsulta() == TipoConsulta.SELECT ) {
            return 1;
        }
        return 0;
    }

}
