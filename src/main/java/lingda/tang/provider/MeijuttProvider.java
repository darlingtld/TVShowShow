package lingda.tang.provider;

import lingda.tang.pojo.DownloadLink;
import lingda.tang.pojo.Show;
import lingda.tang.service.LocalFileService;
import lingda.tang.util.Config;
import lingda.tang.util.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by darlingtld on 2015/1/14.
 */
@Component
public class MeijuttProvider extends SourceProvider {

    @Autowired
    private LocalFileService localFileService;

    @Override
    public List<Show> searchShows(String keyword, JProgressBar progressBar, int slice) {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString(0 + "%");
        Map<String, String> params = new HashMap<String, String>();
        keyword = magicProcess(keyword);
        params.put("searchword", keyword);
        Document doc = searchShowFromSourceProvider(String.format("%s/%s", Config.HOST_NAME, Config.SEARCH_PATH), params);
        List<Show> showList = new ArrayList<Show>();
        Elements elements = doc.getElementsByClass("cn_box2");
        for (Element element : elements) {
            int value = progressBar.getValue() + 100 / slice / elements.size();
            progressBar.setValue(value);
            progressBar.setString(value + "%");
            showList.add(extractInfo(element));
        }
        Collections.sort(showList, new Comparator<Show>() {
            @Override
            public int compare(Show o1, Show o2) {
                if (o1.getSeason() > o2.getSeason()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return showList;
    }

    private String magicProcess(String keyword) {
        StringBuffer finalKeyword = new StringBuffer();
        for (String word : keyword.split("\\s+")) {
            if (word.matches("[a-zA-Z]+")) {
                finalKeyword.append(word.trim() + " ");
            }
        }
        return finalKeyword.toString().trim();
    }

    private Show extractInfo(Element element) {
        String showName = element.getElementsByClass("B").text();
        String showEnglishName = element.getElementsByTag("font").get(1).text();
        int season = Utils.extractSeasonFromFileName(showName);
        Show show = new Show(showName.substring(0, showName.lastIndexOf("第")), season, 1);
        int start = showEnglishName.indexOf("/") == -1 ? 0 : showEnglishName.indexOf("/") + 1;
        int end = showEnglishName.toLowerCase().lastIndexOf("season");
        show.setEnglishName(showEnglishName.substring(start, end == -1 ? showEnglishName.length() - 1 : end).trim());
        return show;
    }

    public List<DownloadLink> fetchDownloadLinks(String showName, int showSeason) {
        List<DownloadLink> downloadLinks = new ArrayList<DownloadLink>();
//  search
        Map<String, String> params = new HashMap<String, String>();
        String searchword = magicProcess(showName);
        params.put("searchword", searchword);
        Document doc = searchShowFromSourceProvider(String.format("%s/%s", Config.HOST_NAME, Config.SEARCH_PATH), params);
//  search specific shows
//        int startSeason = localFileService.getLastUpdatedShow(showName).getSeason();
//        for (int season = startSeason; season <= showSeason; season++) {
//            if (season != showSeason) continue;
        List<DownloadLink> downloadLinkList = getDownloadLinksFromDocument(doc, showName, Utils.convertNumberToChinese(showSeason));
//            if (downloadLinkList == null || downloadLinkList.isEmpty()) {
//                break;
//            } else {
        downloadLinks.addAll(downloadLinkList);
//            }
//        }
        return downloadLinks;
    }

    private List<DownloadLink> getDownloadLinksFromDocument(Document doc, String showName, String season) {
        List<DownloadLink> downloadLinks = new ArrayList<DownloadLink>();
        Elements elements = doc.getElementsByAttributeValue("title", Utils.getShowNameChinese(showName) + "第" + season + "季");
        String showUrl = getShowUrl(elements.attr("href"));
        Utils.log("Connecting to %s/%s ...", Config.HOST_NAME, showUrl);
        int retry = 5;
        while (--retry >= 0) {
            try {
                elements = Jsoup.connect(String.format("%s/%s", Config.HOST_NAME, showUrl)).timeout(10 * 1000).get().getElementsByTag("script");
                Iterator<Element> iterator = elements.iterator();
                while (iterator.hasNext()) {
                    Element element = iterator.next();
                    if (element.html().contains("GvodUrls1")) {
                        List<DownloadLink> links = processDownLoadLinks(element.html(), showName);
                        for (DownloadLink link : links) {
                            link.setHighDefinition(false);
                        }
                        downloadLinks.addAll(links);
                    }
                    if (element.html().contains("GvodUrls2")) {
                        List<DownloadLink> links = processDownLoadLinks(element.html(), showName);
                        for (DownloadLink link : links) {
                            link.setHighDefinition(true);
                        }
                        downloadLinks.addAll(links);
                    }
                }
                // means that we only have downloadlinks of one kind. this might confuse the users
                if (!downloadLinks.isEmpty() && (downloadLinks.get(0).isHighDefinition() == downloadLinks.get(downloadLinks.size() - 1).isHighDefinition())) {
                    List<DownloadLink> linkList = new ArrayList<DownloadLink>();
                    for (DownloadLink dll : downloadLinks) {
                        DownloadLink newDll = dll.clone();
                        newDll.setHighDefinition(!dll.isHighDefinition());
                        linkList.add(newDll);
                    }
                    downloadLinks.addAll(linkList);
                }
                return downloadLinks;
            } catch (SocketTimeoutException e) {
                Utils.log("Connecting to %s times out", Config.HOST_NAME);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getShowUrl(String url) {
        if (url.startsWith("http")) {
            return url.substring(url.indexOf("content"));
        } else {
            return url;
        }
    }

    private List<DownloadLink> processDownLoadLinks(String linkString, String showName) {
        List<DownloadLink> links = new ArrayList();
        String[] strSplit = linkString.split("down");
        int season = Utils.extractSeasonFromFileName(strSplit[0]);
        for (String s : strSplit) {
            try {
                Show show = localFileService.getLastUpdatedShow(showName);
                if (season < show.getSeason()) continue;
                int episode = Utils.extractEpisodeFromFileName(s);
                if (episode <= show.getEpisode()) continue;
                String link = extractDownLinks(s);
                links.add(new DownloadLink(show.getName(), season, episode, link));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return links;
    }

    private String extractDownLinks(String s) {
        int start = s.indexOf("$") + 1;
        int end = s.indexOf("|/$");
        String downloadLink = end == -1 ? s.substring(start) : s.substring(start, end);
        return downloadLink.endsWith("$") ? downloadLink.substring(0, downloadLink.length() - 2) : downloadLink;
    }
}
