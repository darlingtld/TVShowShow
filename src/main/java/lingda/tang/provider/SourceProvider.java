package lingda.tang.provider;

import lingda.tang.pojo.Show;
import lingda.tang.util.Utils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

/**
 * Created by darlingtld on 2015/1/14.
 */
public abstract class SourceProvider {

    public abstract List<Show> searchShows(String keyword, JProgressBar progressBar, int slice);

    public Document searchShowFromSourceProvider(String url, Map<String, String> params) {
        int retry = 7;
        Utils.log("Connecting to %s ...", url);
        while (--retry >= 0) {
            try {
                Connection connection = Jsoup.connect(url).timeout(10 * 1000);
                if (params != null && !params.isEmpty()) {
                    connection = connection.data(params);
                }
                Document doc = connection.post();
                return doc;
            } catch (SocketTimeoutException e) {
                Utils.log("Connecting to %s times out", url);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return null;
    }
}
