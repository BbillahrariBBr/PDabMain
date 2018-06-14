package test.com.potherdabai.potherdabimain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {
    public String readUrl(String myurl) throws IOException {
        String data = "";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(myurl).openConnection();
            httpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while (true) {
                str = bufferedReader.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str);
            }
            data = sb.toString();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

