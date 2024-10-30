import org.example.web.Api;
import org.example.processing.ResponseProcessing;
import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты для класса ResponseProcessing
 */
public class ResponseProcessingTest {

    /**
     * Тестировать обрезку текста от html тегов
     */
    @Test
    public void cutTextTest() {
        ResponseProcessing processing = new ResponseProcessing();
        String text = new Api().getApi();

        String actual = processing.cutText(text);

        Assert.assertFalse(actual.contains("<h1>"));
        Assert.assertFalse(actual.contains("</h1>"));
    }
}
