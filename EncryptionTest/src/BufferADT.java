/**
 * @author Thomas Dowey tdowey3
 * @version 10-27-2014 Improved ADT for buffer pools using the buffer-passing
 *          style. Most user functionality is in the buffer class, not the
 *          buffer pool itself. A single buffer in the buffer pool
 */
public interface BufferADT {
    /**
     * * @param pos - position within buffer
     * @param len - number of bytes to get
     * @return the data Read the associated block from disk (if necessary) and
     * return a pointer to the data
     */
    public byte[] getData(int pos, int len);

    /**
     * @param space - data to be added to buffer
     * @param pos - position where data needs to be written to Flag buffer's
     * contents as having changed, so that flushing the block will
     * write it back to disk
     */
    public void markDirty(byte[] space, int pos);
}