package info.xiaomo.gameCore.base.common;

public class EncryptUtil {
	
	private static Encryptor encryptor = null;
	
	public static byte[] encrypt(byte[] bytes) {
		if(encryptor == null){
			return bytes;
		}
		return encryptor.encrypt(bytes);
	}
	
	public static byte[] decrypt(byte[] bytes) {
		if(encryptor == null){
			return bytes;
		}
		return encryptor.decrypt(bytes);
	}
	
	public static void initEncryptor(Encryptor encryptor) {
		EncryptUtil.encryptor = encryptor;
	}
	
	
}
