package info.xiaomo.gengine.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求工具
 *
 * @version $Id: $Id
 */
public final class HttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

	private HttpUtil() {
	}

	/**
	 * <p>URLPost.</p>
	 *
	 * @param strUrl  a {@link String} object.
	 * @param content a {@link String} object.
	 * @return a {@link ByteArrayOutputStream} object.
	 * @throws IOException if any.
	 */
	public static ByteArrayOutputStream URLPost(String strUrl, String content) throws IOException {
		return URLPost(strUrl, null, content.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * <p>URLPost.</p>
	 *
	 * @param strUrl  a {@link String} object.
	 * @param content an array of {@link byte} objects.
	 * @return a {@link ByteArrayOutputStream} object.
	 * @throws IOException if any.
	 */
	public static ByteArrayOutputStream URLPost(String strUrl, byte[] content) throws IOException {
		return URLPost(strUrl, null, content);
	}

	/**
	 * POST METHOD
	 *
	 * @param strUrl      String
	 * @param contentType a {@link String} object.
	 * @param content     Map
	 * @return String
	 * @throws IOException
	 */
	public static ByteArrayOutputStream URLPost(String strUrl, String contentType, byte[] content) throws IOException {

		URL url = new URL(strUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		try {
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			if (contentType != null) {
				con.setRequestProperty("Content-Type", contentType);
			} else {
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			}
			con.setRequestProperty("Content-Length", String.valueOf(content.length));
			con.setConnectTimeout(30000);// jdk 1.5换成这个,连接超时
			con.setReadTimeout(30000);// jdk 1.5换成这个,读操作超时

			try (OutputStream outputStream = con.getOutputStream()) {
				outputStream.write(content);
				outputStream.flush();
			}

			int responseCode = con.getResponseCode();

			if (HttpURLConnection.HTTP_OK == responseCode) {
				byte[] buffer = new byte[512];
				int len = -1;
				try (InputStream is = con.getInputStream()) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while ((len = is.read(buffer)) != -1) {
						bos.write(buffer, 0, len);
					}
					return bos;
				}
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return null;
	}

	/**
	 * get请求
	 *
	 * @param strUrl a {@link String} object.
	 * @return a {@link String} object.
	 */
	public static String URLGet(String strUrl) {
		ByteArrayOutputStream baos;
		try {
			baos = URLGet(strUrl, 30000);
			if (baos == null) {
				return "";
			}
			return new String(baos.toByteArray(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			LOGGER.error("HTTP GET REQUEST", e);
		}

		return "";

	}

	/**
	 * GET METHOD
	 *
	 * @param strUrl  String
	 * @param timeout a int.
	 * @return List
	 * @throws IOException
	 */
	public static ByteArrayOutputStream URLGet(String strUrl, int timeout) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			con.setUseCaches(false);
			con.setConnectTimeout(timeout);// jdk 1.5换成这个,连接超时
			con.setReadTimeout(timeout);// jdk 1.5换成这个,读操作超时
			HttpURLConnection.setFollowRedirects(true);
			int responseCode = con.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				byte[] buffer = new byte[512];
				int len = -1;
				is = con.getInputStream();
				bos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				return bos;

			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
			con.disconnect();
			if (is != null) {
				is.close();
			}
		}
		return bos;
	}

	/**
	 * 充值相关的HTTP请求使用 GET METHOD
	 *
	 * @param strUrl String
	 * @return List
	 * @throws IOException
	 */
	public static ByteArrayOutputStream URLGetByRecharge(String strUrl) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			con.setUseCaches(false);
			con.setConnectTimeout(7000);// jdk 1.5换成这个,连接超时
			con.setReadTimeout(7000);// jdk 1.5换成这个,读操作超时
			HttpURLConnection.setFollowRedirects(true);
			int responseCode = con.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				byte[] buffer = new byte[512];
				int len = -1;
				is = con.getInputStream();
				bos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				return bos;

			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
			con.disconnect();
			Objects.requireNonNull(is).close();
		}
		return bos;
	}

	/**
	 * <p>httpPost.</p>
	 *
	 * @param url       a {@link String} object.
	 * @param paramsMap a {@link Map} object.
	 * @return a {@link String} object.
	 */
	public static String httpPost(String url, Map<String, String> paramsMap) {
		return httpPost(url, paramsMap, null);
	}

	/**
	 * http的post请求
	 *
	 * @param url       a {@link String} object.
	 * @param paramsMap a {@link Map} object.
	 * @param headMap   a {@link Map} object.
	 * @return a {@link String} object.
	 */
	public static String httpPost(String url, Map<String, String> paramsMap,
	                              Map<String, String> headMap) {
		String responseContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
			setPostHead(httpPost, headMap);
			setPostParams(httpPost, paramsMap);
			try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
				HttpEntity entity = response.getEntity();
				responseContent = getRespString(entity);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return responseContent;
	}

	/**
	 * 设置POST的参数
	 *
	 * @param httpPost
	 * @param paramsMap
	 * @throws Exception
	 */
	private static void setPostParams(HttpPost httpPost, Map<String, String> paramsMap)
			throws Exception {
		if (paramsMap != null && paramsMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = paramsMap.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		}
	}

	/**
	 * 设置http的HEAD
	 *
	 * @param httpPost
	 * @param headMap
	 */
	private static void setPostHead(HttpPost httpPost, Map<String, String> headMap) {
		if (headMap != null && headMap.size() > 0) {
			Set<String> keySet = headMap.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, headMap.get(key));
			}
		}
	}

	/**
	 * 将返回结果转化为String
	 *
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	private static String getRespString(HttpEntity entity) throws Exception {
		if (entity == null) {
			return null;
		}
		InputStream is = entity.getContent();
		StringBuilder strBuf = new StringBuilder();
		byte[] buffer = new byte[4096];
		int r;
		while ((r = is.read(buffer)) > 0) {
			strBuf.append(new String(buffer, 0, r, StandardCharsets.UTF_8));
		}
		return strBuf.toString();
	}
}
