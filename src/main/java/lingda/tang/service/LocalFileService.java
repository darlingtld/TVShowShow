package lingda.tang.service;

import lingda.tang.pojo.Show;
import lingda.tang.util.Config;
import lingda.tang.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darlingtld on 2015/1/5.
 */
@Service
public class LocalFileService {

    private Map<String, Show> lastUpdateShowMap = new HashMap<String, Show>();

    public void init() {
        initLastUpdateShowMap(new File(Config.LOCAL_SHOW_PATH));
        for (Show show : Utils.getShowList()) {
            if (!lastUpdateShowMap.containsKey(show.getEnglishName())) {
                lastUpdateShowMap.put(show.getEnglishName(), new Show(show.getName(), 1, 0));
            }
        }
        for (Map.Entry<String, Show> e : lastUpdateShowMap.entrySet()) {
            Utils.log(e.getKey() + " -> S" + e.getValue().getSeason() + "E" + e.getValue().getEpisode());
        }

    }

    public Show getLastUpdatedShow(String showName) {
//        return lastUpdateShowMap.get(showName);
        return new Show("",0,0);
    }

    private void initLastUpdateShowMap(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                initLastUpdateShowMap(f);
            }
        } else {
            String showName = getShowNameFromFileName(file.getName());
            if (showName == null) {
//                System.out.println(String.format("%s is not to be updated, ", file.getName()));
            } else {
                // get episode and season information
                int season = Utils.extractSeasonFromFileName(file.getName());
                int episode = Utils.extractEpisodeFromFileName(file.getName());
                Show show = new Show(showName, season, episode);
                if (lastUpdateShowMap.get(showName) == null) {
                    lastUpdateShowMap.put(show.getName(), show);
                } else {
                    Show oldShow = lastUpdateShowMap.get(show.getName());
                    if (show.getSeason() > oldShow.getSeason()) {
                        lastUpdateShowMap.put(show.getName(), show);
                    }
                    if (show.getEpisode() > oldShow.getEpisode()) {
                        lastUpdateShowMap.put(show.getName(), show);
                    }
                }

            }
        }
    }

    private String getShowNameFromFileName(String fileName) {
        for (Show show : Utils.getShowList()) {
            if (fileName.contains(show.getName())) {
                return show.getEnglishName();
            }
        }
        return null;
    }


}
