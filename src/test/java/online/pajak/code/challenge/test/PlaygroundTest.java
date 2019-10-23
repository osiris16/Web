package online.pajak.code.challenge.test;

import lombok.Data;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlaygroundTest {


    @Data
    public static class TaxScheme implements Comparable<TaxScheme>{
        BigDecimal from;
        BigDecimal to;
        BigDecimal taxPersen;


        public TaxScheme(BigDecimal from, BigDecimal to, BigDecimal taxPersen){
            super();
            this.from = from;
            this.to = to;
            this.taxPersen = taxPersen;

        }

        @Override
        public int compareTo(TaxScheme o) {
            BigDecimal compareTo = ((TaxScheme) o).getTo();

            //ascending order
            return compareTo.subtract(this.getTo()).intValue();
        }
    }

    @Data
    public static class TaxReliefs{
        String typeReliefs;
        BigDecimal reliefs;

        public TaxReliefs(String typeReliefs, BigDecimal reliefs){
            this.reliefs =reliefs;
            this.typeReliefs = typeReliefs;
        }
    }


    public Map<String, TaxReliefs> generateRelieft(){

        //YOU CAN GET FROM MASTER DATA (DB , API, OR PROPERTIES)
        Map<String, TaxReliefs> ret = new HashMap<>();

        TaxReliefs ls1 = new TaxReliefs("Single", BigDecimal.valueOf(54000000));
        ret.put("TKO", ls1);

        TaxReliefs ls2 = new TaxReliefs("Married with no dependant", BigDecimal.valueOf(58500000));
        ret.put("K0", ls2);

        TaxReliefs ls3 = new TaxReliefs("Married with 1 dependant", BigDecimal.valueOf(63000000));
        ret.put("K1", ls3);

        TaxReliefs ls4 = new TaxReliefs("Married with 2 dependant", BigDecimal.valueOf(67500000));
        ret.put("K2", ls4);

        TaxReliefs ls5 = new TaxReliefs("Married with 3 dependant", BigDecimal.valueOf(72000000));
        ret.put("K3", ls5);

        return ret;
    }


    @Test
    public void ChooseTaxRate(){

        //YOU CAN GET FROM MASTER DATA (DB , API, OR PROPERTIES)
        TaxScheme[]  listData = new TaxScheme[4];
        TaxScheme lis1 = new TaxScheme(BigDecimal.ZERO,BigDecimal.valueOf(50000000), BigDecimal.valueOf(5).divide(BigDecimal.valueOf(100)));
        listData[0] = lis1;
        TaxScheme lis2 = new TaxScheme(BigDecimal.valueOf(50000000),BigDecimal.valueOf(250000000), BigDecimal.valueOf(15).divide(BigDecimal.valueOf(100)));
        listData[1] = lis2;
        TaxScheme lis3 = new TaxScheme(BigDecimal.valueOf(250000000),BigDecimal.valueOf(500000000), BigDecimal.valueOf(25).divide(BigDecimal.valueOf(100)));
        listData[2] = lis3;
        TaxScheme lis4 = new TaxScheme(BigDecimal.valueOf(500000000),BigDecimal.valueOf(9999999999l), BigDecimal.valueOf(30).divide(BigDecimal.valueOf(100)));
        listData[3] = lis4;

        Arrays.sort(listData);

        BigDecimal mountlyIncome = BigDecimal.valueOf(25000000);


        System.out.println("hasil Perhitungan 2 : "+   taxPersen( listData, mountlyIncome.multiply(BigDecimal.valueOf(12))));
        Map<String, TaxReliefs> ret = generateRelieft();

        System.out.println("hasil Perhitungan 3 : "+   taxPersen( listData, mountlyIncome.multiply(BigDecimal.valueOf(12)).subtract(ret.get("TKO").getReliefs())));


    }
    private BigDecimal taxPersen(TaxScheme[] listData, BigDecimal annualIncome){
        BigDecimal hasil = BigDecimal.ZERO;
            for (TaxScheme tx : listData){
            //    System.out.println(tx);
                BigDecimal sisasebelum = BigDecimal.ZERO;
                if(annualIncome.intValue() >= tx.getFrom().intValue()  && annualIncome.intValue() <= tx.getTo().intValue()){
                    sisasebelum = annualIncome.subtract(tx.getFrom());
                    System.out.print("rumus : "+hasil+" + ("+sisasebelum+" x "+tx.taxPersen);
                    hasil = hasil.add(sisasebelum.multiply(tx.taxPersen));
                    System.out.println(" = "+hasil);
                    annualIncome = annualIncome.subtract(sisasebelum);
                }
            }
        return hasil;
    }
}
