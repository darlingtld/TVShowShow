package lingda.tang.provider;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import lingda.tang.pojo.BTLink;
import lingda.tang.pojo.Show;
import lingda.tang.util.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by darlingtld on 2015/2/6.
 */
@Component
public class DygodProvider extends SourceProvider {
    private static final String DYGOD_DOMAIN = "http://www.dy2018.com";

    @Override
    public List<Show> searchShows(String keyword, JProgressBar progressBar, int slice) {
        List<Show> showList = new ArrayList<Show>();
        String resultUrl = getSearchResultUrl(keyword);
        Document doc = Utils.connect(resultUrl, null);
        Elements elements = doc.select(".co_content8 table");
        for (int i = 0; i < elements.size(); i++) {
            int progress = progressBar.getValue() + 100 / slice / elements.size();
            progressBar.setValue(progress);
            progressBar.setStringPainted(true);
            progressBar.setString(progress + "%");
            Element element = elements.get(i);
            Element aTagElement = element.getElementsByClass("ulink").first();
            Show show = new Show(aTagElement.text());
            try {
                show.setUrl(new URL(DYGOD_DOMAIN + aTagElement.attr("href")));
                String date = element.getElementsByTag("font").last().text();
                date = date.substring(0, date.indexOf("点击"));
                show.setDescription(String.format("%s\t%s", date, element.getElementsByTag("td").last().text()));
                show.setBtLinkList(processDownLoadLinks(show));
                showList.add(show);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                continue;
            }
        }

        return showList;
    }

    private List<BTLink> processDownLoadLinks(Show show) throws Exception {
        Document document = Utils.connect(show.getUrl().toString(), null);
        BTLink btLink = new BTLink();
        btLink.setDescription(show.getName());
        btLink.setUrl(document.select("a[href~=ftp]").first().attr("href"));
        return Arrays.asList(btLink);
    }

    private String getSearchResultUrl(String keyword) {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        // Get the first page
        try {
            final HtmlPage page1 = webClient.getPage(DYGOD_DOMAIN);
            // Get the form that we are dealing with and within that form,
            // find the submit button and the field that we want to change.
            final HtmlForm form = page1.getFormByName("searchform");
            final HtmlSubmitInput button = form.getInputByName("Submit");
            final HtmlTextInput textField = form.getInputByName("keyboard");
            // Change the value of the text field
            textField.setValueAttribute(keyword);
            // Now submit the form by clicking the button and get back the second page.
            HtmlPage page2 = button.click();
            return page2.getUrl().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webClient.closeAllWindows();
        }
        return null;
    }

}
