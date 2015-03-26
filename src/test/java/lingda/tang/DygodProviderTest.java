package lingda.tang;

import lingda.tang.pojo.Show;
import lingda.tang.provider.DygodProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;
import java.util.List;

/**
 * Created by darlingtld on 2015/1/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class DygodProviderTest {
    @Autowired
    private DygodProvider dygodProvider;

    @Test
    public void searchShowTest() {
        List<Show> showList = dygodProvider.searchShows("异兽禁区", new JProgressBar(),1);

    }
}
