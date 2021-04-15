package Extras.Request;

import Extras.JSON.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
    final public int status;
    final public HashMap<String, String> headers;
    final public HashMap<String, String> cookies;
    final public String response;
    final public String error;

    public HTTPResponse (HttpURLConnection conn) throws IOException {
        this.status = conn.getResponseCode();

        InputStream input = conn.getInputStream();
        InputStream error = conn.getErrorStream();

        if (input == null) {
            this.response = null;
        } else {
            this.response = new String(conn.getInputStream().readAllBytes());
        }

        if (error == null) {
            this.error = null;
        } else {
            this.error = new String(conn.getErrorStream().readAllBytes());
        }

        this.headers = new HashMap<>();
        Map<String, List<String>> headerMap = conn.getHeaderFields();
        for (Map.Entry<String, List<String>> header: headerMap.entrySet()) {
            this.headers.put(header.getKey(), String.join(", ", header.getValue()));
        }

        this.cookies = new HashMap<>();
        String cookieString = conn.getHeaderField("Set-Cookie");
        if (cookieString != null) {
            List<HttpCookie> cookieList = HttpCookie.parse(cookieString);
            cookieList.forEach(x -> cookies.put(x.getName(), x.getValue()));
        }

        conn.disconnect();
    }

    public JSONObject getJSON () {
        return new JSONObject(response);
    }

    @Override
    public String toString() {
        return "HTTPResponse {" +
                "status = " + status +
                ", headers = " + headers +
                ", cookies = " + cookies +
                '}';
    }
}
