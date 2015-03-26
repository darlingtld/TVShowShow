package lingda.tang.service;

import lingda.tang.engine.MovieEngine;
import lingda.tang.provider.BttiantangProvider;
import lingda.tang.provider.DygodProvider;
import lingda.tang.provider.MeijuttProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by darlingtld on 2015/2/4.
 */
public class InstanceHolder {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
    private static MeijuttProvider mjttProvider = context.getBean(MeijuttProvider.class);
    private static LocalFileService localFileService = context.getBean(LocalFileService.class);
    private static BttiantangProvider bttiantangProvider = context.getBean(BttiantangProvider.class);
    private static DygodProvider dygodProvider = context.getBean(DygodProvider.class);
    private static MovieEngine movieEngine = context.getBean(MovieEngine.class);

    public static MeijuttProvider getMjttProvider() {
        return mjttProvider;
    }

    public static LocalFileService getLocalFileService() {
        return localFileService;
    }

    public static BttiantangProvider getBttiantangProvider() {
        return bttiantangProvider;
    }

    public static DygodProvider getDygodProvider() {
        return dygodProvider;
    }

    public static MovieEngine getMovieEngine() {
        return movieEngine;
    }
}
