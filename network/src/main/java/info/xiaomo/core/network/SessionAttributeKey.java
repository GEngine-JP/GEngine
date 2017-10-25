package info.xiaomo.core.network;

import io.netty.util.AttributeKey;

/**
 * @author xiaomo
 */
public interface SessionAttributeKey {

    /**
     * 用户ID
     */
    AttributeKey<Long> UID = AttributeKey.valueOf("UID");

    AttributeKey<String> LOGINNAME = AttributeKey.valueOf("loginName");

    AttributeKey<Integer> SERVERID = AttributeKey.valueOf("serverId");
}
