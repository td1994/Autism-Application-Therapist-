package application;
/**
 * @author Thomas Dowey tdowey3
 * @version 10-27-2014 Buffer class - stores and edits data from disk
 */
public class Buffer implements BufferADT {
    /**
     * the block
     */
    public byte[] buff;
    /**
     * status if data was written to this block
     */
    public boolean isDirty;
    /**
     * label for the block
     */
    public int number;

    /**
     * @param number - block number in the disk
     * @param data - data read from disk constructor 
     * constructor - gets data from disk and stores it
     */
    public Buffer(int number, byte[] data) {
        buff = data;
        this.number = number;
        isDirty = false;
    }

    /**
     * * @param pos - position within buffer
     * @param len - number of bytes to get
     * @return the data Read the associated block from disk (if necessary) and
     * return a pointer to the data
     */
    @Override
    public byte[] getData(int pos, int len) {
        // gets data from position in the block
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = buff[pos + i];
        }
        return data;
    }

    /**
     * @param space - data to be added to buffer
     * @param pos - position where data needs to be written to Flag buffer's
     * contents as having changed, so that flushing the block will
     * write it back to disk
     */
    @Override
    public void markDirty(byte[] space, int pos) {
        // writes over data in a position within the block and
        // sets it to dirty
        for (int i = 0; i < space.length; i++) {
            buff[pos + i] = space[i];
        }
        isDirty = true;
    }

}
