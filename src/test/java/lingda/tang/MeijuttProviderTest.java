package lingda.tang;

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
import java.util.List;

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
        List<Show> showList = meijuttProvider.searchShows("Grey", new JProgressBar(), 1);
        Utils.log(showList.get(0).toString());
    }
}
