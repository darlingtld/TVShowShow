package lingda.tang;

import lingda.tang.pojo.Show;
import lingda.tang.provider.MeijuttProvider;
import lingda.tang.service.LocalFileService;
import lingda.tang.util.Config;
import lingda.tang.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {

    @Autowired
    private LocalFileService localFileService;

    @Autowired
    private MeijuttProvider meijuttProvider;

    @Test
    public void testConvertNumberToChinese() {
        for (int i = 0; i < 30; i++) {
            System.out.println(i + " -> " + Utils.convertNumberToChinese(i));
        }
    }

    @Test
    public void testInitLastUpdateShowMap() {
        localFileService.toString();
    }

    @Test
    public void testSearchKeyword() {
        List<Show> showList = meijuttProvider.searchShows("vampire", new JProgressBar(), 1);
        for (Show show : showList) {
            Utils.log("%s, %s", show.getName(), show.getSeason());
        }
    }

    @Test
    public void rwShowRepository() throws UnsupportedEncodingException {
//        String shows = new String(Config.SHOW_REPOSITORY.getBytes(),"gbk");
//        String shows = Config.SHOW_REPOSITORY;
//        Utils.log(shows);
        Map<String, Show> showMap = new HashMap<String, Show>();
        showMap.put("Revenge", new Show("复仇", 1, 0));
        Config.storeProperty(showMap);
    }

    @Test
    public void testResolveThunderLinks() {
        String s = "QUFlZDJrOi8vfGZpbGV8JUU1JUE0JThEJUU0JUJCJTg3LlJldmVuZ2UuUzA0RTE0LiVFNCVCOCVBRCVFOCU4QiVCMSVFNSVBRCU5NyVFNSVCOSU5NS5XRUItSFIuQUMzLjEwMjRYNTc2LngyNjQubWt2fDUxMTI1MjU5N3w3MTBmZjA3NGQ2NWVkZDYwZjFjODc0YTkyZGEzZTk4ZHxoPXA1eW1ldTd3ZXNuZ3ZjY2NtemJpeDc0eXU1NDdmdmlhfC9aWg=";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            Utils.log(new String(b));
        } catch (Exception e) {
        }
    }
}

