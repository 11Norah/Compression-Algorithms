import Compression.HuffmanCompression;
import Decompression.Decompression;

import java.io.*;
import java.util.Objects;

import FileUtils.FileUtils;


public class Main {

    public static void main(String[] args) {
        if (Objects.equals(args[0], "c")) {
            String fileToBeCompressed = args[1];
            int n = Integer.parseInt(args[2]);
            String fileToBegenerated = FileUtils.getOutputFileStreamForCompression(fileToBeCompressed, n);
            FileOutputStream fileOutputStream;
            FileInputStream fileInputStream;
            try {
                fileOutputStream = new FileOutputStream(fileToBegenerated);
                fileInputStream = new FileInputStream(fileToBeCompressed);
                long start = System.currentTimeMillis();
                HuffmanCompression huffmanCodeGenerator = new HuffmanCompression(n);
                huffmanCodeGenerator.compress(fileInputStream, fileOutputStream);
                long end = System.currentTimeMillis();
                System.out.println("file input size " + fileToBeCompressed.length());
                System.out.println("compressed " + fileToBegenerated.length());

                System.out.println("Compression time = " + (end - start) + " ms");
                File file = new File(fileToBegenerated);
                File file1 = new File(fileToBeCompressed);
                double compressionRatio = (double) file.length() /(double)  file1.length();
                System.out.println("The compressionRatio = "+compressionRatio);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            String fileToBeDecompressed = args[1];
            FileOutputStream fileOutputStream = FileUtils.getOutputFileStreamForDecompression(fileToBeDecompressed);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            FileInputStream fileInputStream;
            try {
                long start = System.currentTimeMillis();
                fileInputStream = new FileInputStream(fileToBeDecompressed);
                Decompression decoder = new Decompression(fileInputStream, bufferedOutputStream);
                decoder.decode();
                long end = System.currentTimeMillis();
                System.out.println("Decompression time = " + (end - start)  + " m");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

