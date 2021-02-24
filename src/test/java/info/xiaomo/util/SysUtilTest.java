package info.xiaomo.util;

import info.xiaomo.gengine.utils.SysUtil;
import org.junit.Test;

/**
 * 系统工具测试
 *
 * <p>2017年10月12日 下午2:28:07
 */
public class SysUtilTest {

    @Test
    public void testJvmInfo() {
        System.err.println(SysUtil.jvmInfo("\r\n"));
        //		System.err.println(SysUtil.jvmInfo("<br>"));
    }
}
