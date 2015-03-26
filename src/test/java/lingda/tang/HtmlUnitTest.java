package lingda.tang;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by darlingtld on 2015/2/6.
 */
public class HtmlUnitTest {

    @Test
    public void submittingForm() {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        // Get the first page
        HtmlPage page1 = null;
        try {
            page1 = webClient.getPage("http://www.dy2018.com");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.

        final HtmlForm form = page1.getFormByName("searchform");

        final HtmlSubmitInput button = form.getInputByName("Submit");
        final HtmlTextInput textField = form.getInputByName("keyboard");

        // Change the value of the text field
        textField.setValueAttribute("亲爱的");

        // Now submit the form by clicking the button and get back the second page.
        HtmlPage page2 = null;
        try {
            page2 = button.click();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(page2.getUrl());

        webClient.closeAllWindows();
    }
}
