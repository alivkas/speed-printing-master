package org.example.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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

        typo.clearTypo();
        Assertions.assertFalse(typos.containsKey("Random text 1"));
        Assertions.assertFalse(typos.containsValue("Random text 1"));
        Assertions.assertFalse(typos.containsKey("Random text 2"));
        Assertions.assertFalse(typos.containsValue("Random text 2"));

        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");

        int count = typo.countNumberOfTypos();
        Assertions.assertEquals(4, count);

        typo.clearTypo();
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
        typo.saveTypo("Typo for this text_5", "hhhhhh ]]");

        List<String> actual = typo.markTypo();
        List<String> expected = new ArrayList<>();
        expected.add("Typ-o-forthistext_0");
        expected.add("Typ-o-+a+f-o-+g+rthistex-t-+d+_1");
        expected.add("Typof-o-+g+rth-i-stex-t-+d+_2");
        expected.add("T+s+ypoforthistext_3");
        expected.add("T+sd+ypoforthistext_4");
        expected.add("-Typo-+hhhhhh+-for-+]]+");

        Assertions.assertEquals(expected, actual);
    }

    /**
     * Тестирует получение количества ошибок за тренировку
     */
    @Test
    public void countNumberOfTyposTest() {
        typo.saveTypo("Typo for this text_0", "Typ for this text_0");
        typo.saveTypo("Typo for this text_1", "Typa fgr this texd_1");
        typo.saveTypo("Typo for this text_2", "Typo fgr ths texd_2");
        typo.saveTypo("Typo for this text_3", "Tsypo for this text_3");
        typo.saveTypo("Typo for this text_4", "Tsdypo for this text_4");
        typo.saveTypo("Typo for this text_5", "hhhhhh ]]");

        int actual = typo.countNumberOfTypos();

        Assertions.assertEquals(13, actual);
    }
}
