package com.qifeng.will.base.util;

import com.qifeng.will.base.vo.BaseConstant;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * HTTP通讯工具
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static Charset defaultCharset = Consts.UTF_8;

    private static HashMap<String, String> defaultGetRequestHeader = new HashMap<String, String>();
    static {
        defaultGetRequestHeader.put("Content-Type", "text/plain;charset=" + defaultCharset.name());
        defaultGetRequestHeader.put("Accept-Charset", defaultCharset.name());
        defaultGetRequestHeader.put(
                "Accept",
                "application/json,text/xml,application/xml,text/plain");
        defaultGetRequestHeader.put("Accept-Encoding", "gzip, deflate");
        defaultGetRequestHeader.put("Connection", "close");
    }

    private static HashMap<String, String> defaultPostRequestHeader = new HashMap<String, String>();
    static {
        defaultPostRequestHeader.put("Content-Type", "application/x-www-form-urlencoded;charset="
                + defaultCharset.name());
        defaultPostRequestHeader.put("Accept-Charset", defaultCharset.name());
        defaultPostRequestHeader.put(
                "Accept",
                "application/json,text/xml,application/xml,text/plain");
        defaultPostRequestHeader.put("Accept-Encoding", "gzip, deflate");
        defaultPostRequestHeader.put("Connection", "close");
    }
    
    /**
     * 判断是否是ajax请求
     * @param @param request
     * @param @return   
     * @return boolean    
     * @throws
     */
    public static boolean isAjax(HttpServletRequest request){
        return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())   ) ;
    }
    
    public static boolean isPost(HttpServletRequest request){
        if ("POST".equals(request.getMethod().toUpperCase())){
            return true;
        }
        return false;
    }

    public static String doGet(String url, Integer timeout) {
        return doGet(url, null, timeout);
    }
    /**
     * @return
     */
    public static String doGet(String url, Map<String, String> paramsMap, Integer timeout) {
        HttpClient client = null;
        try {
        	if(url.startsWith("https")) {
                client = new SSLClient();
        	} else {
                client = new DefaultHttpClient();
        	}
            if (null != timeout) {
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            }
            HttpGet get = new HttpGet(url);
            StringBuilder uri = new StringBuilder(get.getURI().toString());
            if (!CheckEmptyUtil.isEmpty(paramsMap)){
                String str = EntityUtils.toString(new UrlEncodedFormEntity(generatNameValuePair(paramsMap), "UTF-8"));  
                uri.append(BaseConstant.Separate.QUESTION).append(str);
            }
            get.setURI(new URI(uri.toString()));  
            HttpResponse resp = client.execute(get);  
            HttpEntity entity = resp.getEntity();
            String respContent = EntityUtils.toString(entity , "UTF-8").trim();  
            get.abort();
            return respContent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
    
    /**
     * 获取请求URL.
     * @param request 请求.
     * @return 请求Map集合.
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getRequestPara(HttpServletRequest request, boolean encoding) {
        HashMap<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (!CheckEmptyUtil.isEmpty(requestParams)){
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                if (encoding) {
                    //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                    try {
                        valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                params.put(name, valueStr);
            }
        }
        return params;
    }
    
    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static List<NameValuePair> generatNameValuePair(Map<String, String> properties) {
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(properties.size()); 
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return valuePairs;
    }

    public static void resetGetRequestHeaders(HashMap<String, String> requestHeader,
            HttpUriRequest request) {
        if (defaultGetRequestHeader != null) {
            Iterator<String> requestHeaderNames = defaultGetRequestHeader.keySet().iterator();
            while (requestHeaderNames.hasNext()) {
                String headerName = requestHeaderNames.next();
                String headerValue = defaultGetRequestHeader.get(headerName);
                logger.debug("default request header:{}={}", headerName, headerValue);
                request.setHeader(headerName, headerValue);
            }
        }

        if (requestHeader != null) {
            Iterator<String> requestHeaderNames = requestHeader.keySet().iterator();
            while (requestHeaderNames.hasNext()) {
                String headerName = requestHeaderNames.next();
                String headerValue = requestHeader.get(headerName);
                logger.debug("add or override request header:{}={}", headerName, headerValue);
                request.setHeader(headerName, headerValue);
            }
        }
    }
    
    /**
     * 带超时的POST请求
     * @param uri
     * @param params
     * @param timeout
     * @return
     */
    public static String doPost(String uri, Map<String, String> params, Integer timeout){
        return doPost(uri, params, null, timeout);
    }
    
    /**
     * 不带超时的post请求
     * @param uri
     * @param params
     * @return
     */
    public static String doPost(String uri, Map<String, String> params){
        return doPost(uri, params, null);
    }

    /**
     * post请求
     * @param uri
     * @param params
     * @param requestHeader
     * @param timeout
     * @return
     */
    public static String doPost(String uri, Map<String, String> params,
            HashMap<String, String> requestHeader, Integer timeout) {

        long start = System.currentTimeMillis();

        String responseMsg = null;

        HttpEntity entity = null;
        try {
            HttpClient httpclient = null;
        	if(uri.startsWith("https")) {
        		httpclient = new SSLClient();
        	} else {
        		httpclient = new DefaultHttpClient();
        	}
            
            if (timeout != null) {
                httpclient.getParams().setParameter(
                        CoreConnectionPNames.CONNECTION_TIMEOUT,
                        timeout);
                httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            }
            HttpResponse response = null;

            HttpPost httpPost = new HttpPost(uri);

            resetPostRequestHeaders(requestHeader, httpPost);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            if (params != null) {
                Iterator<String> paramNames = params.keySet().iterator();
                while (paramNames.hasNext()) {

                    String paramName = paramNames.next();
                    String paramValue = params.get(paramName);

                    logger.debug("Request parameter:{}={}", paramName, paramValue);

                    if (!"".equals(paramValue) && null != paramValue) {
                        nvps.add(new BasicNameValuePair(paramName, paramValue));
                    } else {
                        logger.debug("Null value, ignore parameter name {}", paramName);
                    }
                }
            }
            Header[] reqheaders = httpPost.getAllHeaders();
            for (int i = 0; i < reqheaders.length; i++) {
                logger.debug("request header:{}", reqheaders[i]);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, defaultCharset));
            logger.debug("executing request:" + httpPost.getRequestLine());
            response = httpclient.execute(httpPost);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                logger.debug("response header:{}", headers[i]);
            }

            StatusLine statusLine = response.getStatusLine();
            logger.debug("response status:{}", statusLine);

            // 判断页面返回状态判断是否进行转向抓取新链接
            int statusCode = statusLine.getStatusCode();
            if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                    || (statusCode == HttpStatus.SC_SEE_OTHER)
                    || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                // 此处重定向处理 此处还未验证
                String newUri = response.getLastHeader("Location").getValue();

                logger.debug("Redirect to {}", newUri);

                response = httpclient.execute(new HttpPost(newUri));
            }

            entity = response.getEntity();
            if (entity != null) {
                logger.debug("Response content length: " + entity.getContentLength());
                responseMsg = EntityUtils.toString(entity, defaultCharset);
            } else {
                logger.debug("Http response entity is null.");
            }

            logger.debug("responseMsg:{}", responseMsg);

        } catch (Exception e) {
            logger.error("Error while sending and receiving message.", e);
        } finally {
            // close stream
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.warn("Ignore! Error while sending and receiving message.", e);
            }
            long end = System.currentTimeMillis();
            logger.debug("send message by http POST total used {} ms", (end - start));
        }

        return responseMsg;

    }
    
    /**
     * post方式提交请求 \ strURL请求地址\ argsMap参数键值对
     * */
    public static byte[] doPost(String strURL, byte[] b) throws Exception {
        // StringBuffer sbReturn = new StringBuffer("");
        URL url = null;
        HttpURLConnection httpConnection = null;
        InputStream in = null;
        OutputStream out = null;
        BufferedReader br = null;
        byte[] data = null;

        try {
            url = new URL(strURL);
            httpConnection = (HttpURLConnection) url.openConnection();

            httpConnection.setRequestMethod("POST");
//          httpConnection.setRequestProperty($property_name_contentType,
//                  $property_value_contentType);
            httpConnection.setRequestProperty("Cache-Control",
                    "no-cache");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.connect();

            // 发送请求
            out = httpConnection.getOutputStream();
            out.write(b, 0, b.length);
            out.flush();
            out.close();

            // 接收返回
            in = httpConnection.getInputStream();
            data = IOUtils.toByteArray(in);
            /*
             * br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             * String strRead = ""; while ((strRead = br.readLine()) != null) {
             * sbReturn.append(strRead); sbReturn.append($line_feed); }
             */
        } catch (IOException ex) {
            logger.debug(ex.getMessage(), ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException fx) {
                logger.debug(fx.getMessage(), fx);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException fx) {
                logger.debug(fx.getMessage(), fx);
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException fx) {
                logger.debug(fx.getMessage(), fx);
            }
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return data;
    }

    public static void resetPostRequestHeaders(HashMap<String, String> requestHeader,
            HttpUriRequest request) {
        if (defaultPostRequestHeader != null) {
            Iterator<String> requestHeaderNames = defaultPostRequestHeader.keySet().iterator();
            while (requestHeaderNames.hasNext()) {
                String headerName = requestHeaderNames.next();
                String headerValue = defaultPostRequestHeader.get(headerName);
                logger.debug("default request header:{}={}", headerName, headerValue);
                request.setHeader(headerName, headerValue);
            }
        }

        if (requestHeader != null) {
            Iterator<String> requestHeaderNames = requestHeader.keySet().iterator();
            while (requestHeaderNames.hasNext()) {
                String headerName = requestHeaderNames.next();
                String headerValue = requestHeader.get(headerName);
                logger.debug("add or override request header:{}={}", headerName, headerValue);
                request.setHeader(headerName, headerValue);
            }
        }
    }

	public static String read(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        String html = sb.toString();
		logger.debug("request:{}", html);
        return html;
	}
    
	public static void write(String html, HttpServletResponse response) {
		try {
			logger.debug("response:{}", html);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().write(html);
		} catch (IOException e) {
			logger.error("response回写失败", e);
		}
	}
	
	// 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)
	public static InputStream getInputStream(String remoteUrl) {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;

		try {
			URL url = new URL(remoteUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置网络连接超时时间
			httpURLConnection.setConnectTimeout(3000);
			// 设置应用程序要从网络连接读取数据
			httpURLConnection.setDoInput(true);

			httpURLConnection.setRequestMethod("GET");
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == 200) {
				// 从服务器返回一个输入流
				inputStream = httpURLConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inputStream;

	}
	
	/**
	 * 单文件上传
	 * @param remoteUrl 远程url
	 * @param input 文件流
	 * @param fieldName 接收的字段名
	 * @param filename 上传文件名
	 * @return
	 */
	public static boolean uploadFileRemote(String remoteUrl, InputStream input, String fieldName, String filename) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			try {
				HttpPost httppost = new HttpPost(remoteUrl);

				// FileBody bin = new FileBody();
				InputStreamBody bin = new InputStreamBody(input, filename);

				HttpEntity reqEntity = MultipartEntityBuilder.create().addPart(fieldName, bin).build();

				httppost.setEntity(reqEntity);

				logger.debug("executing request " + httppost.getRequestLine());
				CloseableHttpResponse response = httpclient.execute(httppost);
				try {
					logger.debug(response.getStatusLine().toString());
					if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300){
						return true;
					}
//					HttpEntity resEntity = response.getEntity();
//					if (resEntity != null) {
//						logger.debug("Response content length: " + resEntity.getContentLength());
//					}
//					EntityUtils.consume(resEntity);
				} finally {
					response.close();
//		            try {
//		                EntityUtils.consume(reqEntity);
//		            } catch (IOException e) {
//		                logger.warn("Ignore! Error while sending and receiving message.", e);
//		            }
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return false;
	}
}
