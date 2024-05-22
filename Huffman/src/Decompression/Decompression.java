package Decompression;

import Compression.HuffmanCompNode;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Decompression {
    private int numOfByes;
    private final FileInputStream fileInputStream;
    private final BufferedOutputStream bufferedOutputStream;

    public Decompression(FileInputStream fileInputStream, BufferedOutputStream bufferedOutputStream) {
        this.fileInputStream = fileInputStream;
        this.bufferedOutputStream = bufferedOutputStream;
        try {
            this.numOfByes = readInteger();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void decode() throws IOException {
        while (fileInputStream.available() != 0) {
            chunkLoop();
        }
        bufferedOutputStream.flush();
    }

    private void chunkLoop() throws IOException {
        int treeLength = readInteger(); //in bytes
        int dataLength = readInteger(); //in bytes
        int treeLeadingZeroes = readInteger();
        int dataLeadingZeroes = readInteger();
        byte[] fullChuck = fileInputStream.readNBytes(dataLength + treeLength);
        Chunk chunk = new Chunk(fullChuck, dataLength, treeLength, this.numOfByes, treeLeadingZeroes, dataLeadingZeroes);
        decodeData(chunk.getDataBits(), chunk.getRoot());
    }

    private void decodeData(String data, HuffmanDecompNode root) throws IOException {
        int i = 0;
        HuffmanDecompNode node = root;
        while (i < data.length()) {
            if (data.charAt(i) == '0') node = node.getLeft();
            else node = node.getRight();
            if (node.isLeaf()) {
                bufferedOutputStream.write(node.getInput().getBytes());
                node = root;
            }
            i++;
        }
    }

    private int readInteger() throws IOException {
        int i;
        byte[] nBytes = fileInputStream.readNBytes(4);
        i = ByteBuffer.wrap(nBytes).getInt();
        return i;
    }


}
