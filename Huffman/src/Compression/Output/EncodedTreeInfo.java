package Compression.Output;

public class EncodedTreeInfo {
    private byte[] treeBytes;
    private int leadingZeroesOfLastByte;

    public EncodedTreeInfo(byte[] treeBytes, int leadingZeroesOfLastByte) {
        this.treeBytes = treeBytes;
        this.leadingZeroesOfLastByte = leadingZeroesOfLastByte;
    }

    public byte[] getTreeBytes() {
        return treeBytes;
    }

    public void setTreeBytes(byte[] treeBytes) {
        this.treeBytes = treeBytes;
    }

    public int getLeadingZeroesOfLastByte() {
        return leadingZeroesOfLastByte;
    }

    public void setLeadingZeroesOfLastByte(int leadingZeroesOfLastByte) {
        this.leadingZeroesOfLastByte = leadingZeroesOfLastByte;
    }
}
