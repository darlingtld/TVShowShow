package lingda.tang.pojo;

/**
 * Created by darlingtld on 2015/1/14.
 */
public class DownloadLink implements Cloneable{

    private String showName;
    private int season;
    private int episode;
    private String url;

    public boolean isHighDefinition() {
        return isHighDefinition;
    }

    public void setHighDefinition(boolean isHighDefinition) {
        this.isHighDefinition = isHighDefinition;
    }

    private boolean isHighDefinition = false;

    public DownloadLink(String showName, int season, int episode, String url) {
        this.showName = showName;
        this.season = season;
        this.episode = episode;
        this.url = url;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public DownloadLink clone()  {
        try {
            return (DownloadLink) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
