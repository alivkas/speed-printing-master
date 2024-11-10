package org.example.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals("Random tet", typos.get("Random text"));
    }

    /**
     * Тестировать очистку словаря и обнуление счетчика опечаток
     */
    @Test
    public void clearTypoAndTypoCountTest() {
        String originalText1 = "Random text 1";
        String userText1 = "Random text 1 from user";
        String originalText2 = "Random text 2";
        String userText2 = "Random text 2 from user";

        typo.saveTypo(originalText1, userText1);
        typo.saveTypo(originalText2, userText2);

        Map<String, String> typos = typo.getTypos();
        Assertions.assertEquals("Random text 1 from user", typos.get("Random text 1"));
        Assertions.assertEquals("Random text 2 from user", typos.get("Random text 2"));

        typo.clearTypoAndTypoCount();
        Assertions.assertFalse(typos.containsKey("Random text 1"));
        Assertions.assertFalse(typos.containsValue("Random text 1"));
        Assertions.assertFalse(typos.containsKey("Random text 2"));
        Assertions.assertFalse(typos.containsValue("Random text 2"));

        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");

        int count = typo.countNumberOfTypos();
        Assertions.assertEquals(4, count);

        typo.clearTypoAndTypoCount();
        Assertions.assertEquals(0, typo.countNumberOfTypos());
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
        typo.saveTypo("Typo for this text_4", "Tsdypo for this text_4");
        typo.saveTypo("Typo for this text_5", "qrwerqwrqrq 2421");

        Map<String, String> actual = typo.markTypo();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("Typ for this text_0", "   ^                 ");
        expected.put("Typa fgr this texd_1", "   ^  ^          ^   ");
        expected.put("Typo fgr ths texd_2", "      ^   ^    ^  ");
        expected.put("Tsypo for this text_3", " ^                   ");
        expected.put("Tsdypo for this text_4", " ^^                 ");
        expected.put("qrwerqwrqrq 2421", "^^^^^^^^^^^^^^^^");

        Assertions.assertEquals(expected, actual);
    }

    /**
     * Тестирует получение количества ошибок за тренировку
     */
    @Test
    public void countNumberOfTyposTest() {
        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");

        int actual = typo.countNumberOfTypos();

        Assertions.assertEquals(4, actual);
    }
}
