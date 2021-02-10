/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.core.network.mina.websocket;

/**
 * Wraps around a string that represents a websocket handshake response from
 * the server to the browser.
 *
 * @author DHRUV CHOPRA
 * @version $Id: $Id
 */
public class WebSocketHandShakeResponse {

	private final String response;

	/**
	 * <p>Constructor for WebSocketHandShakeResponse.</p>
	 *
	 * @param response a {@link String} object.
	 */
	public WebSocketHandShakeResponse(String response) {
		this.response = response;
	}

	/**
	 * <p>Getter for the field <code>response</code>.</p>
	 *
	 * @return a {@link String} object.
	 */
	public String getResponse() {
		return response;
	}
}
