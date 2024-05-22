package Compression;

import FileUtils.Input;

public class HuffmanCompNode implements Comparable<HuffmanCompNode>  {
    private int freq;
    private Input value;
    private HuffmanCompNode left;
    private HuffmanCompNode right;
    private String binaryRepresentation;

    public String getBinaryRepresentation() {
        return binaryRepresentation;
    }

    public void setBinaryRepresentation(String binaryRepresentation) {
        this.binaryRepresentation = binaryRepresentation;
    }

    public HuffmanCompNode(int freq, Input value) {
        this.freq = freq;
        this.value = value;
        binaryRepresentation = "";
    }

    public boolean isLeaf() {
        return (this.left == null) && (this.right == null);
    }


    public HuffmanCompNode() {
        binaryRepresentation = "";
    }

    public void addOneToRep() {
        binaryRepresentation = binaryRepresentation + "1";
    }

    public void addZeroToRep() {
        binaryRepresentation = binaryRepresentation + "0";
    }

    public HuffmanCompNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanCompNode left) {
        this.left = left;
    }

    public HuffmanCompNode getRight() {
        return right;
    }

    public void setRight(HuffmanCompNode right) {
        this.right = right;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public Input getValue() {
        return value;
    }

    @Override
    public int compareTo(HuffmanCompNode o) {
        return Integer.compare(this.freq, o.freq);
    }
}


