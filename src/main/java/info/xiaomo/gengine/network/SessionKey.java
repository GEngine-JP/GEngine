package info.xiaomo.gengine.network;

import io.netty.util.AttributeKey;

/**
 * @author xiaomo
 */
public class SessionKey {
    public static final AttributeKey<ISession> SESSION = AttributeKey.newInstance("SESSION");

    public static final AttributeKey<Boolean> LOGOUT_HANDLED = AttributeKey.newInstance("LOGOUT_HANDLED");
}
