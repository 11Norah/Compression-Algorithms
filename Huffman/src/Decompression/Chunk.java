package Decompression;

import Compression.HuffmanCompNode;

import java.util.Arrays;

public class Chunk {
    private HuffmanDecompNode root;
    private String treeBits, dataBits;
    private int n;
    private final byte[] inputBytes;
    private final int treeLength; // in bytes
    private final int dataLength; // in bytes
    private final int treeLeadingZeroes, dataLeadingZeroes;


    public Chunk(byte[] inputBytes, int dataLength, int treeLength, int n, int treeLeadingZeroes, int dataLeadingZeroes) {
        this.inputBytes = inputBytes;
        this.treeLength = treeLength;
        this.dataLength = dataLength;
        this.treeLeadingZeroes = treeLeadingZeroes;
        this.dataLeadingZeroes = dataLeadingZeroes;
        this.n = n;
        generateBits();
        generateTree();
    }
    public void generateTree() {
        root = TreeDecoder.decodeTree(treeBits, n);
    }

    public String getDataBits() {
        return dataBits;
    }

    private void generateBits() {
        byte[] tree = new byte[treeLength];
        byte[] data = new byte[dataLength];
        int c = 0;
        System.arraycopy(this.inputBytes, 0, tree, 0, treeLength);
        for (int i = treeLength; i < inputBytes.length; i++) {
            data[c++] = this.inputBytes[i];
        }
        getTreeBinaryRepresentation(tree);
        getDataBinaryRepresentation(data);
    }


    private void getTreeBinaryRepresentation(byte[] tree) {
        StringBuilder res = getRepresentation(tree);
        int num = tree[tree.length-1];
        if (num < 0) {
            String bin = Integer.toBinaryString(num), binDash;
            binDash = bin.substring(24, 32);
            res.append(binDash);
        } else{
            res.append("0".repeat(this.treeLeadingZeroes));
            res.append(Integer.toBinaryString(num));
        }
        this.treeBits = res.toString();
    }

    private String completeString(String bin) {
        StringBuilder binBuilder = new StringBuilder(bin);
        while (binBuilder.length() < 8) {
            binBuilder.insert(0, "0");
        }
        return binBuilder.toString();
    }

    private void getDataBinaryRepresentation(byte[] data) {
        StringBuilder res = getRepresentation(data);
        int num = data[data.length-1];
        if (num < 0) {
            String bin = Integer.toBinaryString(num), binDash;
            binDash = bin.substring(24, 32);
            res.append(binDash);
        } else{
            res.append("0".repeat(this.dataLeadingZeroes));
            res.append(Integer.toBinaryString(data[data.length - 1]));
        }
        this.dataBits = res.toString();
    }

    private StringBuilder getRepresentation(byte[] bytes) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < bytes.length - 1; i++) {
            int num = bytes[i];
            if (num < 0) {
                String bin = Integer.toBinaryString(bytes[i]), binDash;
                binDash = bin.substring(24, 32);
                res.append(binDash);
            } else res.append(completeString(Integer.toBinaryString(bytes[i])));
        }
        return res;
    }


    public HuffmanDecompNode getRoot() {
        return root;
    }

}
