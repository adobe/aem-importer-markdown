package io.adobe.udp.markdownimporter.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Rest Client Utility Class
 */
public class RestClient {
//    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    private CloseableHttpClient httpClient = null;
    private HttpGet httpGetRequest = null;

    /**
     * Rest Client constructor
     */
    public RestClient(String url) {
        httpClient = HttpClientBuilder.create().build();
        httpGetRequest = new HttpGet(url);
    }

    /**
     * Rest Client constructor
     */
    public RestClient(String url, Map<String, String> reqParam) {
        String queryString = "";
        try {
            queryString = mapToQueryString(reqParam);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpClient = HttpClientBuilder.create().build();
        String queryParamDelim = (StringUtils.contains(url, "?"))? "&": "?";
        httpGetRequest = new HttpGet(url + queryParamDelim + queryString);
    }

    public void addHeader(String name, String value) {
        httpGetRequest.addHeader(name, value);
    }

    /**
     * Performs the GET
     *
     * @return String
     * @throws IOException
     */
    public RestClientResponse doGet() throws IOException {
        try {
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);
            StatusLine statusLine = httpResponse.getStatusLine();

            RestClientResponse res = new RestClientResponse();
            res.setStatus(statusLine.getStatusCode());

            if (statusLine.getStatusCode() == HttpStatus.SC_OK || statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    res.setJson(getContentAsString(entity.getContent()));
                }
            } else {
            	System.out.println("Problem executing doGet, status code : " + statusLine.getStatusCode());
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * Convert InputStream to String
     *
     * @param is InputStream
     * @return String
     */
    private String getContentAsString(InputStream is) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            int bytesRead;
            BufferedInputStream bis = new BufferedInputStream(is);
            while ((bytesRead = bis.read(buffer)) != -1) {
                result.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                result.flush();
                result.close();
            } catch (IOException ignore) {
            	System.out.println("Exception caught: " + ignore.getMessage());
            }
        }
        System.out.println("*** getContentAsString: " + result.toString());
        return result.toString();
    }

    private String mapToQueryString(Map<String, String> queryParam) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for(HashMap.Entry<String, String> e : queryParam.entrySet()){
            if(sb.length() > 0){
                sb.append('&');
            }
            sb.append(URLEncoder.encode(e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
        }
        return sb.toString();
    }
}
