package lingda.tang.engine;

import lingda.tang.pojo.Show;
import lingda.tang.provider.BttiantangProvider;
import lingda.tang.provider.DygodProvider;
import lingda.tang.provider.SourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by darlingtld on 2015/2/8.
 */
@Component
public class MovieEngine {

    @Autowired
    private BttiantangProvider bttiantangProvider;
    @Autowired
    private DygodProvider dygodProvider;

    private final int ALIVE_TIME = 60;
    private final int RESULT_WAIT_TIME = 15;

    private List<SourceProvider> providerList = new ArrayList<SourceProvider>();

    @PostConstruct
    public void init() {
        providerList.add(bttiantangProvider);
        providerList.add(dygodProvider);
    }

    public List<Show> searchShows(final String keyword, final JProgressBar progressBar) {
        final List<Show> showList = new ArrayList<Show>();
        List<Future> futureList = new ArrayList<Future>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() + 1, ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1, true));
        for (final SourceProvider provider : providerList) {
            futureList.add(threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    showList.addAll(provider.searchShows(keyword, progressBar, providerList.size()));
                }
            }));
        }
        for (Future future : futureList) {
            try {
                future.get(RESULT_WAIT_TIME, TimeUnit.MINUTES.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return showList;
    }
}
