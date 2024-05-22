package Compression.Output;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class OutputHandler {
    private byte currByte;
    private int counter;
    private int leadingZeroes;
    private boolean hasVal;
    private final BufferedOutputStream bufferedOutputStream;

    public OutputHandler(BufferedOutputStream bufferedOutputStream) {
        this.currByte = 0;
        this.hasVal = false;
        this.counter = 0;
        this.leadingZeroes = 0;
        this.bufferedOutputStream = bufferedOutputStream;
    }

    public int getNumberOfBytes() {
        return this.counter;
    }

    private void saveByte() {
        this.counter++;
        this.currByte <<= 1;
        if (!this.hasVal) this.leadingZeroes++;
        try {
            this.bufferedOutputStream.write(this.currByte);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        this.currByte = 0;
        this.hasVal = false;
    }

    public void add(String binaryRepresentation) {
        for (int i = 0; i < binaryRepresentation.length(); i++) {
            writeBit(binaryRepresentation.charAt(i) == '1');
        }
    }

    private void writeBit(boolean bit) {
        if (!this.hasVal) this.leadingZeroes = 0;
        this.hasVal = true;
        this.currByte <<= 1;
        if (bit) this.currByte |= 1;
        else if (this.currByte == 0) this.leadingZeroes++;
        this.counter++;
        if (this.counter == 8) saveByte();
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
            if (bytes.length == 4) this.bufferedOutputStream.write(bytes);
            else {
                for (int i = bytes.length - 1; i >= 0; i--) {
                    res[c--] = bytes[i];
                }
                for (int i = c; i >= 0; i--) {
                    res[c] = 0;
                }
                this.bufferedOutputStream.write(res);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public EncodedTreeInfo parseStringToBytes(String s) {
        int numOfLeadingZeroes = 0;
        ArrayList<Byte> byteArrayList = new ArrayList<>();
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
        if (this.currByte == 0 && hasVal) res--;
        if (hasVal) saveByte();
        return res;
    }

    public void writeToFile() {
        try {
            this.bufferedOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
