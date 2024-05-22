package FileUtils;

import java.io.*;

public class FileUtils {
    public static FileInputStream getFileInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(filePath);
    }

    public static String getOutputFileStreamForCompression(String inputFilePath, int n) {
        File inputFile = new File(inputFilePath);
        String directory = inputFile.getParent();
        String fileName = inputFile.getName();
        String outputFilePath = directory + "/compressed." + n + "." + fileName + ".hc";
        FileOutputStream fileOutputStream = null;
        return outputFilePath;
    }

    public static FileOutputStream getOutputFileStreamForDecompression(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        String directory = inputFile.getParent();
        String fileName = inputFile.getName();
        String outputFilePath = directory + "/extracted." + fileName.substring(0, fileName.length() - 3);
        try {
            return new FileOutputStream(outputFilePath);
        } catch (IOException e) {
            System.out.println("Error Creating File");
            e.printStackTrace();
            return null;
        }
    }

    public static Input[] readInput(int numOfBytes, FileInputStream fileInputStream, int chunkLength) throws IOException {
        int i = 0, k = 0;
        byte[] bytes = fileInputStream.readNBytes(Math.min(chunkLength, fileInputStream.available()));
        int inputSize = bytes.length / numOfBytes;
        Input[] inputs = new Input[inputSize];
        Input temp;
        while (i < bytes.length) {
            try {
                temp = new Input(numOfBytes);
                while (temp.getCounter() < numOfBytes && i < bytes.length) {
                    temp.addByte(bytes[i++]);
                }
                inputs[k++] = temp;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inputs;
    }
}
