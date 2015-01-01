package stag.lang.ref;

import java.nio.ByteBuffer;

public class Utils {
	public static byte[] u2(int value) {
		return ByteBuffer.allocate(2).putShort((short) value).array();
	}
	public static byte[] u4(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static int b2i(byte[] value) {
		return value[0] << 32 | value[1] << 16 | value[2] << 8 | value[3];
	}

	public static short b2s(byte[] value) {
		return (short) (value[0] << 8 | value[1]);
	}
	public static String str(byte[] array) {
		String ret = "";
		for(byte b: array) {
			ret += String.format("%02x ", b);
		}
		return ret;
	}
}
