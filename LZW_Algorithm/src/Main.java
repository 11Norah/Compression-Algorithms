import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the LZW Compression/Decompression tool!");
        System.out.print("Enter 'c' to compress or 'd' to decompress: ");
        String action = scanner.nextLine();

        System.out.print("Enter the path to the file: ");
        //String filePath = scanner.nextLine();
        String filePath = scanner.nextLine().replace("\"", "");

        long start = System.currentTimeMillis();

        if ("c".equalsIgnoreCase(action)) {
            System.out.println("Compressing has started...");
            Compression compress = new Compression();
            compress.compress(filePath);
            System.out.println("Compression completed.");
            long end = System.currentTimeMillis();
            displayTimeAndRatio(start, end, filePath, filePath + ".lzw");

        } else if ("d".equalsIgnoreCase(action)) {
            System.out.println("Decompressing has started...");
            Decompression decompression=new Decompression();
            decompression.decompress(filePath);
            System.out.println("Decompression completed.");
            long end = System.currentTimeMillis();
            String[] s = filePath.split("\\.");
            String path = s[0] + "_decompressed." + s[1];
            displayTimeAndRatio(start, end, filePath.replace(".lzw", ""),path );

        } else {
            System.out.println("Invalid option. Please enter 'c' or 'd'.");
        }

        scanner.close();
    }

    private static void displayTimeAndRatio(long start, long end, String originalFilePath , String resultFilePath) {
        long timeTaken = end - start;
        System.out.println("Time taken: " + timeTaken + " ms");

        File originalFile = new File(originalFilePath);
        File resultFile = new File(resultFilePath);

        if (originalFile.exists() && resultFile.exists()) {
            long originalSize = originalFile.length();
            long resultSize = resultFile.length();
            System.out.printf("Size of Original file is:%d KB\n",originalSize/1024);
            System.out.printf("Size of Compressed file is:%d KB\n",resultSize/1024);
            double ratio = (double) resultSize / originalSize;

            if(ratio==1.0) {System.out.printf("Decompression ratio: %.2f\n", ratio);}
            else{System.out.printf("Compression ratio: %.2f\n", ratio);}
        } else {
            System.out.println("Could not calculate compression/decompression ratio because one of the files does not exist.");
        }
    }
}
