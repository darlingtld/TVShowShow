package lingda.tang.util;

import lingda.tang.pojo.Show;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

/**
 * Created by darlingtld on 2015/1/5.
 */
public class Config {

    private static Properties prop = new Properties();

    static {
        try {
            prop.load(new InputStreamReader(Config.class.getClassLoader().getResourceAsStream("config.properties"), "GBK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String HOST_NAME = prop.getProperty("host_url");
    public static String SEARCH_PATH = prop.getProperty("search_path");
    public static String LOCAL_SHOW_PATH = prop.getProperty("local_show_path");
    public static String SHOW_REPOSITORY = prop.getProperty("show_repository");

    public static void storeProperty(Map<String, Show> showMap) {
        try {
            String url = Config.class.getResource("/config.properties").getFile();
            FileOutputStream fos = new FileOutputStream(url);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Show> e : showMap.entrySet()) {
                sb.append(String.format("%s:%s:%s,", e.getKey(), e.getValue().getName(), e.getValue().getSeason()));
            }
            prop.setProperty("show_repository", sb.toString());
            prop.store(fos, null);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
