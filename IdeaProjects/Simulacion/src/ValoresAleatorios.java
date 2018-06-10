import sun.plugin2.message.MarkTaintedMessage;

import java.lang.Math;

public class ValoresAleatorios {

    public ValoresAleatorios(){

    }

    public double generarTiempoExponencial( double lamnda ){
        return ( -1/lamnda ) * Math.log( 1 - Math.random() );
    }

    public double generarTiempoNormal( double media, double varianza ){
        double z = Math.pow( ( -2 * Math.log( Math.random() ) ), 0.5 ) * Math.cos( 2 * Math.PI * Math.random() );
        return media + ( Math.sqrt(varianza) * z );
    }

    public double generarTiempoUniforme( double minimo, double maximo ){
        return minimo + ( ( maximo - minimo ) * Math.random() ) ;
    }

    public TipoConsulta generarTipoConsulta(){
        double r = Math.random();
        if( r >= 0 && r <= 0.30 ){
            return TipoConsulta.SELECT;
        }else if( r > 0.30 && r <= 0.55 ){
            return TipoConsulta.UPDATE;
        }
        else if( r > 0.55 && r <= 0.90 ){
            return TipoConsulta.JOIN;
        }
        return TipoConsulta.DDL;
    }

}
