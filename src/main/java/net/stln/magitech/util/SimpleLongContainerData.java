package net.stln.magitech.util;

public class SimpleLongContainerData extends LongContainerData {
    private final long[] longs;

    public SimpleLongContainerData(int size) {
        this.longs = new long[size];
    }

    @Override
    public long getLong(int index) {
        return this.longs[index];
    }

    @Override
    public void setLong(int index, long value) {
        this.longs[index] = value;
    }

    @Override
    public int getLongCount() {
        return this.longs.length;
    }
}