package FileUtils;

import java.util.Arrays;
import java.util.Objects;

public class Input {
    private final byte[] bytes;
    private int counter;

    public Input(int n) {
        bytes = new byte[n];
        counter = 0;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void addByte(byte b) {
        bytes[counter++] = b;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Input that = (Input) o;
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(bytes));
    }
}
