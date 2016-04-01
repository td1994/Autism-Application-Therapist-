import java.io.IOException;

/**
 * @author Thomas Dowey tdowey3
 * @version 10-27-2014 ADT for buffer pools using the message-passing style
 */
public interface BufferPoolADT {
    /**
     * @param space - the data to insert to the buffer pool
     * @param sz - size of the data to be inserted
     * @param pos - position to insert to
     * @throws IOException
     * Copy "sz" bytes from "space" to position "pos" in the
     * buffered storage
     */
    public void insert(byte[] space, int sz, int pos) throws IOException;

    /**
     * @param space - the space to write data to
     * @param sz - size of data to get
     * @param pos - position to retrieve data from
     * @throws IOException
     * Copy "sz" bytes from position "pos" of the buffered storage
     * to "space"
     */
    public void getbytes(byte[] space, int sz, int pos) throws IOException;
}