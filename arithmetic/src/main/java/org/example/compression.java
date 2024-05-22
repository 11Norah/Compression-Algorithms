package org.example;

import java.io.* ;
import java.math.BigDecimal ;
import java.math.MathContext ;
import java.math.RoundingMode ;
import java.util.Hashtable ;
import java.util.Map ;

public class compression {

    public static Map<Character, BigDecimal> probabilities ;
    public static Map<Character, BigDecimal> cumulativeProbabilities ;
    final static int precision = 10500 ;
    static MathContext mc = new MathContext(precision, RoundingMode.HALF_UP) ;


    public static String fileReader(String path) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(path)) ;
        StringBuilder text = new StringBuilder() ;
        String line ;

        while ((line = buffer.readLine()) != null) { text.append(line) ; }

        buffer.close() ;
        return text.toString() ;
    }
    public static void fileWriter(String outputFileName, BigDecimal compressedVal, int msgLen) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(outputFileName) );
        output.writeObject(compressedVal.setScale(precision, RoundingMode.HALF_UP)) ;
        output.writeObject(msgLen) ;
        output.writeObject(probabilities) ;
        output.close() ;
    }
    public static Map<Character, BigDecimal> calculateProbabilities(String text){
        Hashtable<Character, Integer> counts = new Hashtable<>() ;
        Map<Character, BigDecimal> probabilities = new Hashtable<>() ;

        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i) ;

            if (counts.containsKey(c)) { counts.put(c, counts.get(c) + 1) ; }
            else { counts.put(c, 1) ; }
        }

        for (Character ch : counts.keySet()) { probabilities.put(ch, BigDecimal.valueOf(counts.get(ch) / (double) text.length())) ; }
        return probabilities ;
    }
    public static Map<Character, BigDecimal> cumulativeProb(String text) {
        Map<Character, BigDecimal> cumulativeProbabilities = new Hashtable<>() ;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i) ;
            BigDecimal val = new BigDecimal("0.0") ;
            for (Character curr : probabilities.keySet()) {
                if (curr < c && curr != '$') {
                    val = val.add(probabilities.get(curr)) ;
                }
            }
            cumulativeProbabilities.put(c, val) ;
        }

        return cumulativeProbabilities ;
    }
    public static BigDecimal compress(String text){
        BigDecimal low = new BigDecimal("0.0") ;
        BigDecimal high = new BigDecimal("1.0") ;
        BigDecimal range = new BigDecimal("1.0") ;

        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i) ;

            BigDecimal cumulative = cumulativeProbabilities.get(c) ;
            BigDecimal range_high = cumulative.add(probabilities.get(c)) ;

            high = (range.multiply(range_high)).add(low) ;
            low = (range.multiply(cumulative).add(low)) ;
            range = high.subtract(low) ;
        }

        return (low.add(high)).divide(new BigDecimal("2.0", mc)) ;
    }
}
