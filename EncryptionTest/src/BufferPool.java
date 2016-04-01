import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Thomas Dowey tdowey3
 * @version 2014-10-27 
 * BufferPool class - maintains data to read and write from disk
 */
public class BufferPool implements BufferPoolADT {
    private static final int BLOCK_SIZE = 4096; // size of a block in bytes
    // the disk where data is being read and written to
    private RandomAccessFile file;
    private Buffer[] buffers; // the buffers that store the data
    private int count; // the number of buffers in the buffer pool
    /**
     * the size of the file in bytes
     */
    long fileSize;
    /**
     * the number of times that data is read from buffers
     */
    long cacheCount;
    /**
     * number of times data is being read from disk
     */
    long diskReadCount;
    /**
     * number of times data is being written to disk
     */
    long diskWriteCount;

    /**
     * @param name - name of the file to read from
     * @param size - size of the buffer pool
     * @throws IOException
     * constructor - sets up disk and variables
     */
    public BufferPool(String name, int size) throws IOException {
        File aFile = new File(name);
        file = new RandomAccessFile(aFile, "rw");
        buffers = new Buffer[size];
        count = 0;
        fileSize = file.length();
        cacheCount = 0;
        diskReadCount = 0;
        diskWriteCount = 0;
    }

    /**
     * @param space - the data to insert to the buffer pool
     * @param sz - size of the data to be inserted
     * @param pos - position to insert to
     * @throws IOException
     * Copy "sz" bytes from "space" to position "pos" in the
     * buffered storage
     */
    @Override
    public void insert(byte[] space, int sz, int pos) throws IOException {
        // the block number where the data needs to be inserted
        int block = pos / BLOCK_SIZE;
        // the position within the block where the data needs to be inserted
        int blockPos = pos % BLOCK_SIZE;
        // the location of the block within the buffer
        int bufferPos = 0;
        // temporary Buffer variable that stores buffer to be moved up front
        Buffer temp = null;
        for (int i = 0; i < count; i++)// checks all buffers
        {
            if (buffers[i].number == block)// if we found the buffer
            {
                // make changes within the buffer
                buffers[i].markDirty(space, blockPos);
                temp = buffers[i]; // set temp to that buffer
                cacheCount++; // increment cache Count
                break; // done with for loop
            }
            else
            {
                //increment position of the buffer
                bufferPos++;
            }
        }
        
        if (temp == null) // if the block is currently not in the buffer
        {
            // create new data array
            byte[] data = new byte[BLOCK_SIZE];
            // retrieve data from the disk
            file.seek(block * BLOCK_SIZE);
            file.read(data, 0, BLOCK_SIZE);
            diskReadCount++; // increment disk read count
            // create new Buffer
            temp = new Buffer(block, data);
            // make changes within the buffer
            temp.markDirty(space, blockPos);
            // if the array is not full, increase the counter
            if (count < buffers.length) {
                count++;
            }
        }

        // if the buffer pool is full
        if (bufferPos == buffers.length) {
            // if there were changes made to the data
            if (buffers[count - 1].isDirty) {
                // write data onto the disk
                file.seek(buffers[count - 1].number * BLOCK_SIZE);
                file.write(buffers[count - 1].buff, 0, BLOCK_SIZE);
                diskWriteCount++; // increment disk write count
            }
            // lower buffer position so it can go through
            bufferPos--;
        }

        // moves all buffers to the left of bufferPos to the right
        for (int i = bufferPos; i > 0; i--) {
            buffers[i] = buffers[i - 1];
        }
        // add in temporary buffer to the front of array
        buffers[0] = temp;
    }

    /**
     * @param space - the space to write data to
     * @param sz - size of data to get
     * @param pos - position to retrieve data from
     * @throws IOException
     * Copy "sz" bytes from position "pos" of the buffered storage
     * to "space"
     */
    @Override
    public void getbytes(byte[] space, int sz, int pos) throws IOException {
        // the block number where the data needs to be inserted
        int block = pos / BLOCK_SIZE;
        // the position within the block where the data needs to be inserted
        int blockPos = pos % BLOCK_SIZE;
        // the location of the block within the buffer
        int bufferPos = 0;
        // temporary Buffer variable that stores buffer to be moved up front
        Buffer temp = null;
        for (int i = 0; i < count; i++) // checks all buffers
        {
            if (buffers[i].number == block) // if we found the buffer
            {
                // get the data from the buffer
                byte[] returnedValue = buffers[i].getData(blockPos, sz);
                for (int j = 0; j < sz; j++) // add it to space
                {
                    space[j] = returnedValue[j];
                }
                // set temp to that buffer
                temp = buffers[i];
                cacheCount++;
                break; // done with for loop
            } 
            else 
            {
                // increment buffer position
                bufferPos++;
            }
        }

        if (temp == null) // if the block is currently not in the buffer
        {
            // create new data array
            byte[] data = new byte[BLOCK_SIZE];
            // retrieve data from the disk
            file.seek(block * BLOCK_SIZE);
            file.read(data, 0, BLOCK_SIZE);
            diskReadCount++;
            // create new Buffer
            temp = new Buffer(block, data);
            // get the data from the buffer
            byte[] returnedValue = temp.getData(blockPos, sz);
            for (int j = 0; j < sz; j++) // add it to space
            {
                space[j] = returnedValue[j];
            }
            // if the array is not full, increase the counter
            if (count < buffers.length) {
                count++;
            }
        }

        // if the buffer pool is full
        if (bufferPos == buffers.length) {
            // if there were changes made to the data
            if (buffers[count - 1].isDirty) {
                // write data onto the disk
                file.seek(buffers[count - 1].number * BLOCK_SIZE);
                file.write(buffers[count - 1].buff, 0, BLOCK_SIZE);
                diskWriteCount++;
            }
            // lower buffer position so it can go through
            bufferPos--;
        }

        // moves all buffers to the left of bufferPos to the right
        for (int i = bufferPos; i > 0; i--) {
            buffers[i] = buffers[i - 1];
        }
        // add in temporary buffer to the front of array
        buffers[0] = temp;
    }

    /**
     * @throws IOException
     *             flushAll - write all remaining buffers to disk once the
     *             quicksort is done
     */
    public void flushAll() throws IOException {
        for (int i = 0; i < count; i++) {
            if (buffers[i].isDirty) {
                file.seek(buffers[i].number * BLOCK_SIZE);
                file.write(buffers[i].buff, 0, BLOCK_SIZE);
                diskWriteCount++;
            }
        }
    }
}
