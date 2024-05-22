import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decompression {
    private static final int INITIAL_DICTIONARY_SIZE = 256; // Initial dictionary size (0-255)

    public static List<Integer> getCompressedInput(String file) {
        List<Integer> compressed = new ArrayList<>();
        try {
            DataInputStream inputFile = new DataInputStream(new FileInputStream(new File(file)));
            while (inputFile.available() > 0) {
                int code = inputFile.readInt();
                compressed.add(code);
            }
            inputFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return compressed;
    }
    public static void writeDecompressedFile(String file, byte[] decompressedData) {
        String[] s = file.split("\\.");
        String path = s[0] + "_decompressed." + s[1];
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path))) {
            outputStream.write(decompressedData);
            System.out.println("File is decompressed");
        } catch (IOException e) {
            throw new RuntimeException("Error writing decompressed file: " + e.getMessage(), e);
        }
    }
    public static HashMap<Integer, List<Byte>> CreateDDictionary() {
        HashMap<Integer, List<Byte>> dictionary = new HashMap<>();
        for (int i = 0; i < INITIAL_DICTIONARY_SIZE; i++) {
            List<Byte> temp = new ArrayList<>();
            temp.add((byte) i);
            dictionary.put(i, temp);
        }
        return dictionary;
    }
    public void decompress(String file) {
        try {
            List<Integer> inputCodes = getCompressedInput(file);
            HashMap<Integer, List<Byte>> dictionary = CreateDDictionary();
            ByteArrayOutputStream decompressed = new ByteArrayOutputStream();

            if (!inputCodes.isEmpty()) {
                List<Byte> currSequence = dictionary.get(inputCodes.get(0));
                decompressed.write(toPrimitiveArray(currSequence));

                for (int i = 1; i < inputCodes.size(); i++) {
                    Integer code = inputCodes.get(i);
                    List<Byte> nextSequence = dictionary.get(code);

                    // If 'nextSequence' is null, it means there's an error in the compressed file or the decompression logic.
                    if (nextSequence == null) {
                        // Special case: the sequence is not in the dictionary.
                        // This can happen when the sequence to be added is the current sequence plus the first character of the current sequence.
                        if (code == dictionary.size()) {
                            nextSequence = new ArrayList<>(currSequence);
                            nextSequence.add(currSequence.get(0));
                        } else {
                            throw new IllegalArgumentException("Invalid compressed code: " + code);
                        }
                    }

                    decompressed.write(toPrimitiveArray(nextSequence));

                    // Create a new sequence by appending the first byte of the next sequence to the current sequence.
                    List<Byte> newSequence = new ArrayList<>(currSequence);
                    newSequence.add(nextSequence.get(0));

                    // Check if the new sequence is already in the dictionary to avoid duplicates.
                    if (!dictionary.containsValue(newSequence)) {
                        dictionary.put(dictionary.size(), newSequence);
                    }

                    currSequence = nextSequence;
                }
            }

            writeDecompressedFile(file, decompressed.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error during decompression: " + e.getMessage(), e);
        }
    }

    private byte[] toPrimitiveArray(List<Byte> byteList) {
        // This method assumes 'byteList' is not null. Callers must check for null before calling this method.
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }
}