package swift.http.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static swift.http.parse.Constant.REGX_BR;

public class HttpResponse {
    private final String version;
    private final int statusCode;
    private final String statusMessage;
    private final Map<String, String> headers;
    private final String body;

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

        String[] lines = sb.toString().split(REGX_BR+REGX_BR);
        String[] headers = lines[0].split(REGX_BR);
        String[] statusLine = headers[0].split(" ");
        String version = statusLine[0];
        int statusCode = Integer.parseInt(statusLine[1]);
        String statusMessage = statusLine.length >2 ?statusLine[2]:"";
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

}

