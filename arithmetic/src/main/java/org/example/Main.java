package org.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class Main {
    public static final int PRECISION = 10500 ;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        String text = compression.fileReader("input12k.txt") ;
        compression.probabilities = compression.calculateProbabilities(text) ;
        System.out.println(compression.probabilities) ;
        compression.cumulativeProbabilities = compression.cumulativeProb(text) ;
        System.out.println(compression.cumulativeProbabilities) ;

        BigDecimal compressed = compression.compress(text) ;

        compression.fileWriter("compressed.dart", compressed, text.length()) ;

        System.out.println(compression.probabilities) ;
        System.out.println(compressed) ;

        //Take object from the decompression class
        long start = System.currentTimeMillis();
        Decompression decompression = new Decompression(PRECISION);
        // Read the compressed file and decode the message
        String decoded = decompression.decompress("compressed.dart");
        System.out.println("Got the decompression ready");
        System.out.println("______________________________________________________________");
        //System.out.println(decoded);
        decompression.saveDecompressedMessage("output.txt",decoded);

        long inBytes = Files.size(Paths.get("input12k.txt"));
        long outBytes = Files.size(Paths.get("compressed.dart"));
        System.out.println("Input file size in bytes "+inBytes);
        System.out.println("Compressed file size in bytes "+outBytes);
        System.out.println("______________________________________________________________");

        double compressionRatio =(outBytes+0.0)/ inBytes;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        String formattedComRatio = decimalFormat.format(compressionRatio);
        System.out.println("Compression ratio "+ formattedComRatio);
        System.out.println("This means that the compressed file is "+formattedComRatio+" times smaller than the original file.");
        System.out.println("______________________________________________________________");

    }

}