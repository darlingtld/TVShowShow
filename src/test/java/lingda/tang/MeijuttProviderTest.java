package lingda.tang;

import lingda.tang.pojo.DownloadLink;
import lingda.tang.pojo.Show;
import lingda.tang.provider.MeijuttProvider;
import lingda.tang.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by darlingtld on 2015/1/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class MeijuttProviderTest {

    @Autowired
    private MeijuttProvider meijuttProvider;

    @Test
    public void testSearchShow() throws UnsupportedEncodingException {
        List<Show> showList = meijuttProvider.searchShows("growing pains season 1", new JProgressBar(), 1);
        Utils.log(showList.get(0).toString());
    }

    @Test
    public void getDownloadLinks() {
        List<Show> showList = meijuttProvider.searchShows("growing pains season 1", new JProgressBar(), 1);
//        Utils.log(showList.get(0).toString());
        for (Show show : showList) {
            Utils.addToShowList(show.getEnglishName(), show);
            Map<String, List<DownloadLink>> linksMap = new HashMap<String, List<DownloadLink>>();
            String showName = show.getEnglishName();
            try {
                linksMap.put(showName, meijuttProvider.fetchDownloadLinks(showName, show.getSeason()));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            List<DownloadLink> downloadLinkList = linksMap.get(showName);
            Collections.reverse(downloadLinkList);
            for (DownloadLink link : downloadLinkList) {
                Utils.log(link.getShowName() + " " + link.getUrl());
            }
        }
    }
}
