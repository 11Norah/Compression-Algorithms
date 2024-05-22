import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compression {
    private static final int INITIAL_DICTIONARY_SIZE = 256; // Initial dictionary size (0-255)
    public static void writeCompressedFile(String file, List<Integer> compressed) {
        File newFile = new File(file + ".lzw");
        try {
            DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(newFile));
            for (int code : compressed) {
                outputFile.writeInt(code);
            }
            outputFile.close();
            System.out.println("File is compressed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<List<Byte>,Integer> CreateCDictionary(){
        HashMap<List<Byte>,Integer> dictionary=new HashMap<>();
        for(int i=0;i<INITIAL_DICTIONARY_SIZE;i++){
            List<Byte> temp = new ArrayList<>();
            temp.add((byte) i);
            dictionary.put(temp,i);
            //else{dict.put(i,temp);}
        }
        return dictionary;
    }

    public void compress(String file_name) {
        HashMap<List<Byte>,Integer> dictionary = CreateCDictionary();
        List<Integer> compressedOutput = new ArrayList<>();
        List<Byte> currentSequence = new ArrayList<>();
        int nextCode = INITIAL_DICTIONARY_SIZE;

        try (FileInputStream inputStream = new FileInputStream(new File(file_name))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte b = buffer[i];
                    List<Byte> nextSequence = new ArrayList<>(currentSequence);
                    nextSequence.add(b);

                    if (dictionary.containsKey(nextSequence)) {
                        currentSequence = nextSequence;
                    } else {
                        Integer code = (Integer) dictionary.get(currentSequence);
                        if (code != null) {
                            compressedOutput.add(code);
                        }
                        dictionary.put(nextSequence, nextCode);
                        nextCode++;
                        currentSequence = new ArrayList<>();
                        currentSequence.add(b);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading input file: " + e.getMessage(), e);
        }

        if (!currentSequence.isEmpty()) {
            Integer code = (Integer) dictionary.get(currentSequence);
            if (code != null) {
                compressedOutput.add(code);
            }
        }

        writeCompressedFile(file_name, compressedOutput);
    }

}