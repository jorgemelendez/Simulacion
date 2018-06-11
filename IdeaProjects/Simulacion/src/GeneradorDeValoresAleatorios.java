import sun.plugin2.message.MarkTaintedMessage;

import java.lang.Math;
import java.util.Random;

public class GeneradorDeValoresAleatorios {

    Random random;
    public GeneradorDeValoresAleatorios(){
        this.random = new Random(1235689);
    }

    public double generarValorExponencial(double lamnda ){
        return ( -1/lamnda ) * Math.log( 1 - /*Math.random()*/this.random.nextDouble() );
    }

    public double generarValorNormal(double media, double varianza ){
        double z = Math.pow( ( -2 * Math.log( /*Math.random()*/this.random.nextDouble() ) ), 0.5 ) * Math.cos( 2 * Math.PI * /*Math.random()*/this.random.nextDouble() );
        return media + ( Math.sqrt(varianza) * z );
    }

    public double generarValorUniforme(double minimo, double maximo ){
        return minimo + ( ( maximo - minimo ) * /*Math.random()*/this.random.nextDouble() ) ;
    }

    public TipoConsulta generarValorTipoConsulta(){
        double r = /*Math.random()*/this.random.nextDouble();
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
