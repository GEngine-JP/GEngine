package info.xiaomo.mina;

import java.nio.ByteOrder;
import org.junit.Test;

/**
 * 解析测试
 *
 * @date 2017-04-01 QQ:359135103
 */
public class DecoderConfigDataTest {

    /** 测试大小端 */
    @Test
    public void testEnding() {
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        System.err.println(byteOrder);
    }
}
