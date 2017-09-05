package com.zhenai.channel_manager.util;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class HttpsclientUtil {

	private static final Logger logger = Logger.getLogger(HttpsclientUtil.class);

	private static final String CHARSET_UTF8 = "UTF-8";

	public static CloseableHttpClient getHttpsClient() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
	}


	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		CloseableHttpClient httpClient = getHttpsClient();
		try {
			HttpPost post = new HttpPost(url);
			HttpEntity entity = map2UrlEncodedFormEntity(params);
			if(entity != null) {
				post.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
			return result;
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String post(String url, String json) {
		CloseableHttpClient httpClient = getHttpsClient();
		try {
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(json, CHARSET_UTF8);
			if(entity != null) {
				post.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
			return result;
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String get(String url) throws UnsupportedEncodingException {
		CloseableHttpClient httpClient = getHttpsClient();
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpClient.execute(get);
			String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
			return result;
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String get(String url, Map<String, Object> param) throws UnsupportedEncodingException {
		String finalUrl = url;
		StringBuilder urlBuilder = new StringBuilder(url);
		if (param != null && !param.isEmpty()) {
			urlBuilder.append("?");
			for (Entry<String, Object> entry : param.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				urlBuilder.append(key).append("=").append(value).append("&");
			}
			finalUrl = urlBuilder.subSequence(0, urlBuilder.length() - 1).toString();
		} else {
			finalUrl = urlBuilder.toString();
		}
		return get(finalUrl);
	}

	/**
	 * 参数转换
	 * @param params
	 * @return
	 */
	public static HttpEntity map2UrlEncodedFormEntity(Map<String, String> params) {
		if(params != null && !params.isEmpty()) {
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			while(iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				nvps.add(new BasicNameValuePair(StringUtils.trimToEmpty(entry.getKey()), StringUtils.trimToEmpty(entry.getValue())));
			}
			try {
				return new UrlEncodedFormEntity(nvps, CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}
		}
		return null;
	}
}

