package lingda.tang.pojo;

import lingda.tang.util.Strings;

import java.net.URL;
import java.util.List;

/**
 * Created by darlingtld on 2015/1/5.
 */
public class Show {
    private int season;
    private int episode;
    private String name;
    private URL url;
    private String englishName;
    //    for movie from bttiantang
    private List<BTLink> btLinkList;
    private String description= Strings.NO_DESCRIPTION;

    public Show(String showName, int season, int episode) {
        this.name = showName;
        this.season = season;
        this.episode = episode;
    }

    public Show(String showName) {
        this.name = showName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BTLink> getBtLinkList() {
        return btLinkList;
    }

    public void setBtLinkList(List<BTLink> btLinkList) {
        this.btLinkList = btLinkList;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getEpisode() {
        return episode;

    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {

        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "Show{" +
                "season=" + season +
                ", episode=" + episode +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", englishName='" + englishName + '\'' +
                ", btLinkList=" + btLinkList +
                ", description='" + description + '\'' +
                '}';
    }
}
