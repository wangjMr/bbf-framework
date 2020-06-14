package cn.bbf.utils;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class HttpClientUtil {
	public static String doPost(String url, Map<String, Object> params, String headerAction) throws Exception {
		String result = null;
		// 设置默认请求体编码
		String charset = "utf-8";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		//拼接参数
		// 设置请求体信息
		if (null != params) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
				httpPost.setEntity(entity);
			}
		}
		HttpResponse response = httpclient.execute(httpPost);
		if (response != null) {
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity, charset);
			}
		}
//			result = ex.getMessage();
//			ex.printStackTrace();
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url, Map<String,Object> params) throws  Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		URIBuilder uriBuilder = new URIBuilder(url);
		for(String key : params.keySet()) {
			uriBuilder.addParameter(key,params.get(key).toString());
		}
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		HttpResponse response = httpclient.execute(httpGet);
		/** 读取服务器返回过来的json字符串数据 **/
		return EntityUtils.toString(response.getEntity(),"UTF-8");
	}

}
