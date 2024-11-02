package org.example.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Тестирование класса Typo
 */
public class TypoTest {

    private final Typo typo = new Typo();

    /**
     * Тестирует сохранение оригинальног текста и текста пользователя в словарь
     */
    @Test
    public void saveTypoTest() {
        String originalText = "Random text";
        String userText = "Random tet";
        typo.saveTypo(originalText, userText);
        Map<String, String> typos = typo.getTypos();

        Assert.assertEquals("Random tet", typos.get("Random text"));
    }

    /**
     * Тестирует очистку словаря
     */
    @Test
    public void clearTypoTest() {
        String originalText1 = "Random text 1";
        String userText1 = "Random text 1";
        String originalText2 = "Random text 2";
        String userText2 = "Random text 2";

        typo.saveTypo(originalText1, userText1);
        typo.saveTypo(originalText2, userText2);

        Map<String, String> typos = typo.getTypos();

        typo.clearTypo();

        Assert.assertFalse(typos.containsKey("Random text 1"));
        Assert.assertFalse(typos.containsValue("Random text 1"));
        Assert.assertFalse(typos.containsKey("Random text 2"));
        Assert.assertFalse(typos.containsValue("Random text 2"));
    }

    /**
     * Тестирует пометку местоположения опечатки
     */
    @Test
    public void findTypoTest() {
        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");
        typo.saveTypo("Typo for this text_2", "Typo fgr ths texd_2");
        typo.saveTypo("Typo for this text_3", "Tsypo for this text_3");

        Map<String, String> actual = typo.markTypo();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("Typ for this text_0", "   ^                 ");
        expected.put("Typa fgr this texd_1", "   ^  ^          ^   ");
        expected.put("Typo fgr ths texd_2", "      ^    ^^    ^   ");
        expected.put("Tsypo for this text_3", " ^                 ");

        Assert.assertEquals(expected, actual);
    }

    /**
     * Тестирует получение количества ошибок за тренировку
     */
    @Test
    public void getTypoCountTest() {
        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");

        int actual = typo.getTypoCount();

        Assert.assertEquals(4, actual);
    }

    /**
     * Тестирует обнуление счетчика
     */
    @Test
    public void clearTypoCountTest() {
        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");

        int count = typo.getTypoCount();
        typo.clearTypoCount();


        Assert.assertEquals(0, typo.getTypoCounts());
    }
}
