package application;

import java.io.IOException;

public class EncryptFile {

	public static void encrypt(String videoPath, int buffer) throws NumberFormatException, IOException {
		int buff = buffer;
		BufferPool pool = new BufferPool(videoPath, buff);
		int count = 0;
		while (count < pool.fileSize) {
			byte[] data = new byte[buff];
			pool.getbytes(data, buff, count);
			for (int i = 0; i < buff; i++) {
				int val = data[i];
				val = ~val & 0xff;
				data[i] = (byte) val;
			}
			pool.insert(data, buff, count);
			count += buff;
		}
		pool.flushAll();
	}
}
