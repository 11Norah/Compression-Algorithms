package Compression.Output;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class OutputHandler {
    private byte currByte;
    private int counter;
    private int leadingZeroes;
    private boolean hasVal;
    private ChunkFormatter chunkFormatter;
    private final BufferedOutputStream bufferedOutputStream;

    public OutputHandler(BufferedOutputStream bufferedOutputStream) {
        this.currByte = 0;
        this.hasVal = false;
        this.counter = 0;
        this.leadingZeroes = 0;
        this.bufferedOutputStream = bufferedOutputStream;
        this.chunkFormatter = new ChunkFormatter();
    }


    public int getNumberOfBytes() {
        return this.chunkFormatter.getChunkSize();
    }

    private void saveByte() {
        chunkFormatter.addByte(this.currByte);
        this.counter = 0;
        this.currByte = 0;
        this.hasVal = false;
    }

    public void add(String binaryRepresentation) {
        for (int i = 0; i < binaryRepresentation.length(); i++) {
            writeBit(binaryRepresentation.charAt(i) == '1');
            if (counter == 8) saveByte();
        }
    }

    private void writeBit(boolean bit) {
        if (!this.hasVal) leadingZeroes = 0;
        this.hasVal = true;
        this.currByte <<= 1;
        counter++;
        if (bit) this.currByte |= 1;
        else if (this.currByte == 0) leadingZeroes++;
    }


    public void writeBytes(byte[] bytes) {
        try {
            bufferedOutputStream.write(bytes);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void write(Integer b) {
        try {
            BigInteger bigInteger = BigInteger.valueOf(b);
            byte[] bytes = bigInteger.toByteArray();
            byte[] res = new byte[4];
            int c = 3;
            if (bytes.length == 4) bufferedOutputStream.write(bytes);
            else {
                for (int i = bytes.length - 1; i >= 0; i--) {
                    res[c--] = bytes[i];
                }
                for (int i = c; i >= 0; i--) {
                    res[c] = 0;
                }
                bufferedOutputStream.write(res);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public EncodedTreeInfo parseStringToBytes(String s) {
        int numOfLeadingZeroes = 0;
        ArrayList<Byte> byteArrayList = new ArrayList<Byte>();
        for (int i = 0; i < s.length(); i += 8) {
            byte num = 0;
            numOfLeadingZeroes = 0;
            for (int j = i; j < i + 8 && j < s.length(); j++) {
                num <<= 1;
                if (s.charAt(j) == '1') num |= 1;
                if (num == 0) {
                    numOfLeadingZeroes++;
                }
            }
            if (num == 0 && numOfLeadingZeroes > 0) numOfLeadingZeroes--;
            byteArrayList.add(num);
        }
        byte[] bytes = new byte[byteArrayList.size()];
        int c = 0;
        for (Byte b : byteArrayList) {
            bytes[c++] = b;
        }
        return new EncodedTreeInfo(bytes, numOfLeadingZeroes);
    }

    public int close() {
        int res = this.leadingZeroes;
        if (this.currByte == 0 && hasVal || this.chunkFormatter.lastAdded == 0 && !hasVal) res--;
        if (hasVal) saveByte();
        return res;
    }


    public void writeToFile() {
        try {
            bufferedOutputStream.write(this.chunkFormatter.getBytes());
            flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void flush() {
        this.counter = 0;
        this.currByte = 0;
        this.hasVal = false;
        this.chunkFormatter = new ChunkFormatter();
        this.leadingZeroes = 0;
    }

}