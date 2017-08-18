package info.xiaomo.gameCore.base.common;

/**
 * 加密解密器
 * @author 张力
 *
 */
public interface Encryptor {

	byte[] encrypt(byte[] bytes);
	
	byte[] decrypt(byte[] bytes);
}
