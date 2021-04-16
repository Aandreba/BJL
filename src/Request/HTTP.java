package Request;

import Extras.Mathx;
import Units.Time;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTP {
    public URL url;
    public String method;
    public HashMap<String, String> headers;
    public HashMap<String, String> params;
    public HashMap<String, String> cookies;
    public Time connectTimeout, readTimeout;
    public boolean followRedirects;

    public HTTP (String method, URL url) {
        this.url = url;
        this.method = method;
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.cookies = new HashMap<>();
        this.followRedirects = false;
    }

    public HTTP (String method, String url) throws MalformedURLException {
        this(method, new URL(url));
    }

    public void setTimeout (Time time) {
        this.connectTimeout = time;
        this.readTimeout = time;
    }

    public void setTimeout (int milliseconds) {
        setTimeout(new Time(milliseconds, Time.Type.Milliseconds));
    }

    private String paramsString () throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,String> param: params.entrySet()) {
            builder.append("&");
            builder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }

        return builder.substring(1);
    }

    private String cookiesString () throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,String> param: cookies.entrySet()) {
            builder.append("; ");
            builder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }

        return builder.substring(2);
    }

    public HTTPResponse sendRequest () throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        if (!params.isEmpty()) {
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(paramsString());
            out.flush();
            out.close();
        }

        for (Map.Entry<String, String> header: headers.entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        if (!cookies.isEmpty()) {
            conn.setRequestProperty("Cookie", cookiesString());
        }

        if (connectTimeout != null) {
            conn.setConnectTimeout(Mathx.roundToInt(connectTimeout.getValue(Time.Type.Milliseconds)));
        }

        if (readTimeout != null) {
            conn.setReadTimeout(Mathx.roundToInt(readTimeout.getValue(Time.Type.Milliseconds)));
        }

        conn.setInstanceFollowRedirects(followRedirects);
        return new HTTPResponse(conn);
    }

    @Override
    public String toString() {
        return "HTTP {" +
                "url = " + url +
                ", method = '" + method + '\'' +
                ", headers = " + headers +
                ", params = " + params +
                ", cookies = " + cookies +
                ", connectTimeout = " + connectTimeout +
                ", readTimeout = " + readTimeout +
                ", followRedirects = " + followRedirects +
                '}';
    }
}
