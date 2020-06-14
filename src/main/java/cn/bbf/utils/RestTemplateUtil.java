/*
 * 版权所有 2017 冠新软件。
 * 保留所有权利。
 */
package cn.bbf.utils;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 工具类
 */
public class RestTemplateUtil {

	/**
	 * 改善Post请求
	 *
	 */
	public static String post(String param, String url) {
		// 调用post接口
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		HttpEntity<String> formEntity = new HttpEntity<>(param, headers);

		String result = restTemplate.postForObject(url, formEntity, String.class);
		return result;
	}

	/**
	 * post 請求帶header
	 * 
	 * @param param
	 * @param url
	 * @param header
	 * @return
	 */
	public static String postWithHeaders(String param, String url, Map<String, String> header) {
		// 调用post接口
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		for (Map.Entry<String, String> entry : header.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}

		HttpEntity<String> formEntity = new HttpEntity<>(param, headers);

		String result = restTemplate.postForObject(url, formEntity, String.class);
		return result;
	}

	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		String result = responseEntity.getBody();
		return result;
	}

	/**
	 * get请求，带header
	 * 
	 * @param url
	 * @param header
	 * @return
	 */
	public static String getWithHeaders(String url, Map<String, String> header) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		for (Map.Entry<String, String> entry : header.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}
		HttpEntity<String> requestEntity = new HttpEntity(null, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String result = responseEntity.getBody();
		return result;
	}
}
