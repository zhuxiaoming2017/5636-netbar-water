package com.water.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * HttpServletRequest帮助类
 */
public class RequestUtils {

    public static final String MOBILENO_KEY = "MOBILENO_KEY";

    private static final Logger log = LoggerFactory
            .getLogger(RequestUtils.class);

    public static Long getRequestKeyToLong(HttpServletRequest request, String key) {
        String value = getQueryParam(request, key);
        return StringUtils.isNotBlank(value) ? new Long(value) : 0l;
    }

    public static Double getRequestKeyToDouble(HttpServletRequest request, String key) {
        String value = getQueryParam(request, key);
        return StringUtils.isNotBlank(value) ? new Double(value) : 0D;
    }

    public static Integer getRequestKeyToInteger(HttpServletRequest request, String key) {
        String value = getQueryParam(request, key);
        return StringUtils.isNotBlank(value) ? Integer.valueOf(value) : 0;
    }

    public static Boolean getRequestKeyToBoolean(HttpServletRequest request, String key) {
        String value = getQueryParam(request, key);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : Boolean.FALSE;
    }

    public static String getRequestKeyToString(HttpServletRequest request, String key) {
        String value = getQueryParam(request, key);
        return StringUtils.isNotBlank(value) ? value.trim() : null;
    }

    public static byte getByteParameter(HttpServletRequest request,String paramName,byte defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Byte.parseByte(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	public static int getIntParameter(HttpServletRequest request,String paramName,int defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Integer.parseInt(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	public static long getLongParameter(HttpServletRequest request,String paramName,long defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Long.parseLong(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	public static double getDoubleParameter(HttpServletRequest request,String paramName,double defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Double.parseDouble(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	

	/**
	 * 从request获取参数名为paramName的参数,并且将其转换为UTF8的格式
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return 如果获取参数则返回正常的参数值,否则返回defaultValue
	 */
	public static String getStringParameter(HttpServletRequest request,String paramName,String defaultValue){
		String value = request.getParameter(paramName);
		if(value == null || StringUtils.isBlank(value.trim())){
			return defaultValue;
		}else{
			if(request.getMethod().equalsIgnoreCase("POST")){
				return value.trim();
			}else{
				try {
					//return new String(value.trim().getBytes("iso-8859-1"),"utf-8");
					
					return value.trim();
				} catch (Exception e) {
					return defaultValue;
				}
			}
		}
	}

	public static float getFloatParameter(HttpServletRequest request,
			String paramName, float defaultValue) {
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Float.parseFloat(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}

    /**
     * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
     * 那么将通过HttpServletRequest#getParameter获取。
     *
     * @param request web请求
     * @param name    参数名称
     * @return
     */
    public static String getQueryParam(HttpServletRequest request, String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (request.getMethod().equalsIgnoreCase("POST")) {
            return request.getParameter(name);
        }
        String s = request.getQueryString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            s = URLDecoder.decode(s, "UTF8");
        } catch (UnsupportedEncodingException e) {
            log.error("encoding " + "UTF8" + " not support?", e);
        }
        String[] values = parseQueryString(s).get(name);
        if (values != null && values.length > 0) {
            return values[values.length - 1];
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getQueryParams(HttpServletRequest request) {
        Map<String, String[]> map;
        if (request.getMethod().equalsIgnoreCase("UTF8")) {
            map = request.getParameterMap();
        } else {
            String s = request.getQueryString();
            if (StringUtils.isBlank(s)) {
                return new HashMap<String, Object>();
            }
            try {
                s = URLDecoder.decode(s, "UTF8");
            } catch (UnsupportedEncodingException e) {
                log.error("encoding " + "UTF8" + " not support?", e);
            }
            map = parseQueryString(s);
        }

        Map<String, Object> params = new HashMap<String, Object>(map.size());
        int len;
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            len = entry.getValue().length;
            if (len == 1) {
                params.put(entry.getKey(), entry.getValue()[0]);
            } else if (len > 1) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }

    /**
     * Parses a query string passed from the client to the server and builds a
     * <code>HashTable</code> object with key-value pairs. The query string
     * should be in the form of a string packaged by the GET or POST method,
     * that is, it should have key-value pairs in the form <i>key=value</i>,
     * with each pair separated from the next by a &amp; character.
     * <p/>
     * <p/>
     * A key can appear more than once in the query string with different
     * values. However, the key appears only once in the hashtable, with its
     * value being an array of strings containing the multiple values sent by
     * the query string.
     * <p/>
     * <p/>
     * The keys and values in the hashtable are stored in their decoded form, so
     * any + characters are converted to spaces, and characters sent in
     * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
     *
     * @param s a string containing the query to be parsed
     * @return a <code>HashTable</code> object built from the parsed key-value
     * pairs
     * @throws IllegalArgumentException if the query string is invalid
     */
    public static Map<String, String[]> parseQueryString(String s) {
        String valArray[] = null;
        if (s == null) {
            throw new IllegalArgumentException();
        }
        Map<String, String[]> ht = new HashMap<String, String[]>();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = (String) st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                continue;
            }
            String key = pair.substring(0, pos);
            String val = pair.substring(pos + 1, pair.length());
            if (ht.containsKey(key)) {
                String oldVals[] = (String[]) ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++) {
                    valArray[i] = oldVals[i];
                }
                valArray[oldVals.length] = val;
            } else {
                valArray = new String[1];
                valArray[0] = val;
            }
            ht.put(key, valArray);
        }
        return ht;
    }

    public static Map<String, String> getRequestMap(HttpServletRequest request,
                                                    String prefix) {
        return getRequestMap(request, prefix, false);
    }

    public static Map<String, String> getRequestMapWithPrefix(
            HttpServletRequest request, String prefix) {
        return getRequestMap(request, prefix, true);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> getRequestMap(
            HttpServletRequest request, String prefix, boolean nameWithPrefix) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> names = request.getParameterNames();
        String name, key, value;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name.startsWith(prefix)) {
                key = nameWithPrefix ? name : name.substring(prefix.length());
                value = StringUtils.join(request.getParameterValues(name), ',');
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 获取访问者IP
     * <p/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p/>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
//	public static String getIpAddr(HttpServletRequest request) {
//		String ip = request.getHeader("X-Real-IP");
//		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//			return ip;
//		}
//		ip = request.getHeader("X-Forwarded-For");
//		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//			// 多次反向代理后会有多个IP值，第一个为真实IP。
//			int index = ip.indexOf(',');
//			if (index != -1) {
//				return ip.substring(0, index);
//			} else {
//				return ip;
//			}
//		} else {
//			return request.getRemoteAddr();
//		}
//	}
    public static String getIpAddr(HttpServletRequest request) {

        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();

    }

    /**
     * 获得当的访问路径
     * <p/>
     * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
     *
     * @param request
     * @return
     */
    public static String getLocation(HttpServletRequest request) {
        UrlPathHelper helper = new UrlPathHelper();
        StringBuffer buff = request.getRequestURL();
        String uri = request.getRequestURI();
        String origUri = helper.getOriginatingRequestUri(request);
        buff.replace(buff.length() - uri.length(), buff.length(), origUri);
        String queryString = helper.getOriginatingQueryString(request);
        if (queryString != null) {
            buff.append("?").append(queryString);
        }
        return buff.toString();
    }

    /**
     * 获取用户手机号码，能获取手机号返回手机号，否则返回空字符串
     *
     * @return 用户手机号码
     */
    public static String getPhone(HttpServletRequest request) {
        boolean isSet = false;
        String mobile = "";
        if (getSessionValue(request, MOBILENO_KEY) != null) {
            mobile = getSessionValue(request, MOBILENO_KEY);
            isSet = true;
        }

        if (!isSet) {
            String phone = request.getHeader("x-up-calling-line-id");

            if (phone != null && phone.startsWith("+86")) {
                mobile = phone.substring(3);
                isSet = true;
            } else if (!isSet && phone != null && phone.startsWith("86")) {
                mobile = phone.substring(2);
                isSet = true;
            } else if (!isSet && phone != null) {
                mobile = phone;
                isSet = true;
            }

            setSessionValue(request, MOBILENO_KEY, mobile);
        }

        return mobile;
    }


    /**
     * @param request
     * @return String型Url
     * @Title: getURL
     * @Description:根据request获取Url
     */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath().equals("/") ? ""
                : request.getContextPath();

        String schame = "http://";

        if (Integer.valueOf(request.getServerPort()) == 443) {
            schame = "https://";
        }

        String url = schame + request.getServerName();
        if (Integer.valueOf(request.getServerPort()) != 80 && Integer.valueOf(request.getServerPort()) != 443)
            url = url + ":"
                    + (Integer.valueOf(request.getServerPort()))
                    + contextPath;
        else {
            url = url + contextPath;
        }
        return url;
    }


    public static String getDomainName(HttpServletRequest request) {
        String schame = "http://";

        if (Integer.valueOf(request.getServerPort()) == 443) {
            schame = "https://";
        }

        String url = schame + request.getServerName();
        if (Integer.valueOf(request.getServerPort()) != 80 && Integer.valueOf(request.getServerPort()) != 443) {
            url = url + ":"
                    + Integer.valueOf(request.getServerPort());
        }

        return url;
    }

    public static String getURL(HttpServletRequest request) {
        return getURI(request) + extractParams(request, 2000);
    }

    public static String getURI(HttpServletRequest request)
            throws IllegalStateException {
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        String ctxPath = helper.getOriginatingContextPath(request);
        int start = 0, i = 0, count = 0;
        if (!StringUtils.isBlank(ctxPath)) {
            count++;
        }

        while (i < count && start != -1) {
            start = uri.indexOf('/', start + 1);
            i++;
        }
        return uri.substring(start);
    }

    public static String extractParams(HttpServletRequest request, int length) {
        String param = "";
        Map params = request.getParameterMap();
        for (Object o : params.keySet()) {
            String key = (String) o;
            String values[] = (String[]) params.get(key);
            for (String value : values) {
                param += key + "=" + value + "&";
            }
        }

        List<String> keys = new ArrayList<String>(params.keySet());

        Collections.sort(keys);
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);

            if (value instanceof Object[]) {
                String values[] = (String[]) params.get(key);
                for (String val : values) {
                    if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                        prestr = prestr + key + "=" + val;
                    } else {
                        prestr = prestr + key + "=" + val + "&";
                    }
                }
            } else {
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }


        }

        return prestr;
    }

    public static String getHttpRequestAttribue(HttpServletRequest request) {
        Enumeration enumerations = request.getAttributeNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (enumerations.hasMoreElements()) {
            String name = (String) enumerations.nextElement();
            params.put(name, request.getAttribute(name));
        }

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);

            if (value instanceof Object[]) {
                String values[] = (String[]) params.get(key);
                for (String val : values) {
                    if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                        prestr = prestr + key + "=" + val;
                    } else {
                        prestr = prestr + key + "=" + val + "&";
                    }
                }
            } else {
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }

        return prestr;
    }

   
    public static String getHttpSessionAttribue(HttpServletRequest request) {
        Enumeration enumerations = request.getSession().getAttributeNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (enumerations.hasMoreElements()) {
            String name = (String) enumerations.nextElement();
            params.put(name, request.getSession().getAttribute(name));
        }

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);

            if (value instanceof Object[]) {
                String values[] = (String[]) params.get(key);
                for (String val : values) {
                    if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                        prestr = prestr + key + "=" + val;
                    } else {
                        prestr = prestr + key + "=" + val + "&";
                    }
                }
            } else {
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }
        prestr += "&jsession" + request.getSession().getId();
        return prestr;
    }

    public static <T> T getSessionValue(HttpServletRequest request, String key) {
        return (T) request.getSession().getAttribute(key);
    }

    public static void setSessionValue(HttpServletRequest request, String key, Object value) {
        request.getSession().setAttribute(key, value);
    }
    public static Map<String, String> getQuerySingleParams(HttpServletRequest request) {
        Map<String, String[]> map;
        if (request.getMethod().equalsIgnoreCase("POST")) {
            map = request.getParameterMap();
        } else {
            String s = request.getQueryString();
            if (StringUtils.isBlank(s)) {
                return new HashMap<String, String>();
            }
            try {
                s = URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("encoding UTF-8 not support?", e);
            }
            map = parseQueryString(s);
        }

        Map<String, String> params = new HashMap<String, String>(map.size());
        int len;
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            len = entry.getValue().length;
            if (len == 1) {
                params.put(entry.getKey(), entry.getValue()[0].trim());
            }
        }
        return params;
    }
   
}
