package Request;

import Extras.CSV;
import Extras.Files;
import JSON.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
    final public int status;
    final public HashMap<String, String> headers;
    final public HashMap<String, String> cookies;
    final public ByteBuffer body;
    final public String response;
    final public String error;

    public HTTPResponse (HttpURLConnection conn) throws IOException {
        this.status = conn.getResponseCode();

        InputStream inputStream = conn.getInputStream();
        InputStream errorStream = conn.getErrorStream();

        if (inputStream == null) {
            this.response = null;
            this.body = null;
        } else {
            byte[] bytes = inputStream.readAllBytes();
            this.body = ByteBuffer.wrap(bytes);
            this.response = new String(bytes);
        }

        if (errorStream == null) {
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

    public CSV getCSV (String separator, char newLine) {
        return new CSV(response, separator, newLine);
    }

    public CSV getCSV (String separator) {
        return new CSV(response, separator);
    }

    public CSV getCSV () {
        return new CSV(response);
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
