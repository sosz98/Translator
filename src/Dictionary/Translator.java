package Dictionary;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Translator {
    private static final String API = "CHANGE ME";

    private final String countryCode;

    public Translator(String countryCode) {
        this.countryCode = countryCode;
    }

    public String translate(String text) throws IOException {
        URL url;
        try {
            url = new URL(String.format("https://api-free.deepl.com/v2/translate?text=%s&source_lang=PL&target_lang=%s&auth_key=%s", text, this.countryCode, API));
        } catch (IOException e) {
            return "Data not found. Check language code.";
        }
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        }
        return new JSONObject(sb.toString()).getJSONArray("translations").getJSONObject(0).getString("text");
    }
}
