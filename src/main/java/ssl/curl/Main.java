package ssl.curl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Support client credentials call via MTLS and introspect call.
 */
public class Main {

    public Main() {

    }

    public static void main(String... args) {

        String url = args[0];
        String data = args[1];
        String headers = args[2];
        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
//        HttpGet get = new HttpGet(url);
//        String response = makeCall(httpClient, get);
//        System.out.println(response);
//        System.exit(1);

//        curl -v -k -u  admin:admin -H 'Content-Type: application/x-www-form-urlencoded'
//                -X POST --data 'token=f19dbc8a-1511-3501-a838-6640fc90bea2' https://localhost:9443/oauth2/introspect

        HttpPost post = new HttpPost(url);
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String[] pair = data.split("=");
        nameValuePairs.add(new BasicNameValuePair(pair[0], pair[1]));
        nameValuePairs.add(new BasicNameValuePair("scope","clearance1 clearance2"));

        String[] headerUnits = headers.split("@");
        Arrays.stream(headerUnits).forEach(param ->
                post.setHeader(param.split("#")[0], param.split("#")[1]));

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            post.setHeader("Content-Type","application/x-www-form-urlencoded");
            String response = makeCall(httpClient, post);
            System.out.println(response);
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String makeCall(CloseableHttpClient httpClient, HttpGet get) {

        try {
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private static String makeCall(CloseableHttpClient httpClient, HttpPost post) {

        try {
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }


}

