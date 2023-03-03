package swift.http.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(String version, int statusCode, String statusMessage, Map<String, String> headers, String body) {
        this.version = version;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body;
    }

    public String getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public static HttpResponse fromInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder sb = new StringBuilder();

        int k = inputStream.available();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }

        String[] lines = sb.toString().split("\\r\\n\\r\\n");
        String[] headers = lines[0].split("\\r\\n");
        String[] statusLine = headers[0].split(" ");
        String version = statusLine[0];
        int statusCode = Integer.parseInt(statusLine[1]);
        String statusMessage = statusLine[2];
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 1; i < headers.length; i++) {
            String[] headerPair = headers[i].split(": ");
            headerMap.put(headerPair[0], headerPair[1]);
        }
        String body = "";
        if (lines.length > 1) {
            body = lines[1];
        }
        return new HttpResponse(version, statusCode, statusMessage, headerMap, body);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %s\r\n", version, statusCode, statusMessage));
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
        }
        sb.append("\r\n");
        sb.append(body);
        return sb.toString();
    }
}

