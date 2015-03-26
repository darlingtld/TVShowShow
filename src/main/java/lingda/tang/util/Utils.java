package lingda.tang.util;

import lingda.tang.pojo.Show;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by darlingtld on 2015/1/5.
 */
public class Utils {

    private static Map<String, Show> showMap = new HashMap<String, Show>();

//    static {
//        String shows = Config.SHOW_REPOSITORY;
//        if (!"".equals(shows.trim())) {
//            for (String showStr : shows.split(",")) {
//                String enName = showStr.split(":")[0];
//                String chineseName = showStr.split(":")[1];
//                int season = Integer.parseInt(showStr.split(":")[2]);
//                Show show = new Show(chineseName, season, 0);
//                show.setEnglishName(enName);
//                showMap.put(enName, show);
//            }
//        }
//
//    }

    public static int extractSeasonFromFileName(String fileName) {
//        第xx季
        Pattern pattern = Pattern.compile("第(\\W+|\\d+)季");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return convertChineseToNumber(matcher.group(1));
        }
//        Sxx
        pattern = Pattern.compile("S(\\d+)E\\d+", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return convertChineseToNumber(matcher.group(1));
        }
        return 0;
    }

    public static int extractEpisodeFromFileName(String fileName) {
        //        第xx集
        Pattern pattern = Pattern.compile("第(\\W+|\\d+)集");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return convertChineseToNumber(matcher.group(1));
        }
//        Exx
        pattern = Pattern.compile("S\\d+E(\\d+)", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return convertChineseToNumber(matcher.group(1));
        }
        return 0;
    }

    //    just deal with two digits number
    public static String convertNumberToChinese(int number) {
        String[] chNumberArray = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        StringBuilder chineseNumber = new StringBuilder();
        int digit1 = number / 10;
        int digit2 = number % 10;
        if (digit1 != 0) {
            if (digit1 != 1) {
                chineseNumber.append(chNumberArray[digit1]);
            }
            chineseNumber.append(chNumberArray[10]);
            if (digit2 != 0) {
                chineseNumber.append(chNumberArray[digit2]);
            }
        } else {
            chineseNumber.append(chNumberArray[digit2]);
        }
        return chineseNumber.toString();
    }

    //    deal with two digits number
    public static int convertChineseToNumber(String chineseNumber) {
        if (chineseNumber.matches("\\d+")) return Integer.parseInt(chineseNumber);
        HashMap<String, Integer> chineseNumberMap = new HashMap();
        for (int i = 0; i < 100; i++) {
            chineseNumberMap.put(convertNumberToChinese(i), i);
        }
        return chineseNumberMap.get(chineseNumber);
    }

    public static final String getShowNameChinese(String key) {
//        for(String keySubStr : key.split("\\s+")){
//            if(!keySubStr.matches("^[a-zA-Z]*")){
//                return key;
//            }
//        }
        return showMap.get(key).getName();
    }

    public static List<Show> getShowList() {
        return new ArrayList<Show>(showMap.values());
    }

    public static void log(String msg, Object... params) {
        if (params.length == 0) {
            System.out.println(msg);
        } else {
            System.out.println(String.format(msg, params));
        }
    }

    public static void addToShowList(String key, Show show) {
        Utils.log("add %s", key);
        showMap.put(key, show);
//        write to configuration file
        Config.storeProperty(showMap);

    }


    public static void deleteFromShowList(String showName) {
        Utils.log("delete %s", showName);
        showMap.remove(showName);
        Config.storeProperty(showMap);
    }

    public static ImageIcon getImageIcon(String path, int width, int height) {
        if (width == 0 || height == 0) {
            return new ImageIcon(Utils.class.getResource(path));
        }
        ImageIcon icon = new ImageIcon(Utils.class.getResource(path));
        icon.setImage(icon.getImage().getScaledInstance(width, height,
                Image.SCALE_DEFAULT));
        return icon;
    }

    public static Document connect(String url, Map<String, String> params) {
        int retry = 5;
        Utils.log("Connecting to %s ...", url);
        while (--retry >= 0) {
            try {
                Connection connection = Jsoup.connect(url).timeout(10 * 1000);
                if (params != null && !params.isEmpty()) {
                    connection = connection.data(params);
                }
                Document doc = connection.get();
                return doc;
            } catch (SocketTimeoutException e) {
                Utils.log("Connecting to %s times out", url);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    public static int getFileLines(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int count = 0;
            while (br.readLine() != null) {
                count++;
            }
            return count;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
