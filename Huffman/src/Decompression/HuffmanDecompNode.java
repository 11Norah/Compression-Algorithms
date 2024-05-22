package Decompression;

import FileUtils.Input;

public class HuffmanDecompNode {
    private Input input;
    private HuffmanDecompNode left;
    private HuffmanDecompNode right;
    private HuffmanDecompNode parent;

    public HuffmanDecompNode(Input input, HuffmanDecompNode parent) {
        this.input = input;
        this.parent = parent;
    }

    public Input getInput() {
        return input;
    }

    public HuffmanDecompNode getParent() {
        return parent;
    }

    public void setParent(HuffmanDecompNode parent) {
        this.parent = parent;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public HuffmanDecompNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanDecompNode left) {
        this.left = left;
    }

    public HuffmanDecompNode getRight() {
        return right;
    }

    public void setRight(HuffmanDecompNode right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return (this.left == null && this.right == null);
    }
}
