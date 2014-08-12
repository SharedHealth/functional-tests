package utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static String post(HttpClient client, String url, String body, Map<String, String> headers){
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new StringEntity(body));
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
            HttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException("error while posing to " + url);
        }
    }

    public static String post(String url, String body, Map<String, String> headers) {
        return post(client(), url, body, headers);
    }

    private static CloseableHttpClient client(String userName, String password){
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    private static org.apache.http.impl.client.CloseableHttpClient client() {
        return HttpClientBuilder.create().build();
    }

    public static String postJson(String url, String json) {
        return post(url, json, jsonHeaders());
    }

    public static String postJson(String url, String json, String userName, String password){
        return post(client(userName, password), url, json, jsonHeaders());
    }

    private static Map<String, String> jsonHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=utf-8");
        return headers;
    }
}
