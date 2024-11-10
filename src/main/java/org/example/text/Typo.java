package org.example.text;

import java.util.*;

/**
 * Взаимодействие с опечатками в тексте
 */
public class Typo {

    private final Map<String, String> typos;

    /**
     * Конструктор Typo, который инициализирует поле typos и typoCount
     */
    public Typo() {
        this.typos = new LinkedHashMap<>();
    }

    /**
     * Сохранить в словарь оригинальный текст как ключ и предложение
     * с опечаткой как значение
     * @param originalText оригинальный текст
     * @param typo опечатка
     */
    public void saveTypo(String originalText, String typo) {
        typos.put(originalText, typo);
    }

    /**
     * Полностью очистить словарь опечаток и обнулить счетчик
     */
    public void clearTypoAndTypoCount() {
        typos.clear();
    }

    /**
     * Получить словарь с опечатками и оригинальным текстом
     * @return список опечаток
     */
    public Map<String, String> getTypos() {
        return typos;
    }

    /**
     * Сохранить опечатки в предложениях и пометки на их местах
     * @return список опечаток с пометками местоположения
     */
    public Map<String, String> markTypo() {
        Map<String, String> marks = new LinkedHashMap<>(typos.size());

        for (Map.Entry<String, String> entry : typos.entrySet()) {
            String correct = entry.getKey();
            String typo = entry.getValue();

            StringBuilder markers = new StringBuilder();

            String[] splitCorrect = correct.split(" ");
            String[] splitTypo = typo.split(" ");

            markupsAlgorithm(splitCorrect, splitTypo, markers);
            marks.put(typo, markers.toString());
        }

        return Collections.unmodifiableMap(marks);
    }

    /**
     * Производит процесс создания пометок на словах с опечатками,
     * сохраняя правильное местоположение. Проверяет случаи опечаток, когда сделана замена символа,
     * его пропуск, лишнее написание и несколько подряд идущих опечаток
     * @param splitCorrect массив слов в оригинальном тексте
     * @param splitTypo массив слов с опечатками
     * @param markers строка для сохранения пометок опечаток
     */
    private void markupsAlgorithm(String[] splitCorrect, String[] splitTypo, StringBuilder markers) {
        int minLength = Math.min(splitCorrect.length, splitTypo.length);

        for (int i = 0; i < minLength; i++) {
            String correctWord = splitCorrect[i];
            String typoWord = splitTypo[i];

            int j = 0;
            int k = 0;

            while (j < correctWord.length()
                    || k < typoWord.length()) {
                if (j < correctWord.length()
                        && k < typoWord.length()) {
                    if (correctWord.charAt(j) == typoWord.charAt(k)) {
                        markers.append(" ");
                    } else {
                        if (k + 1 < typoWord.length()
                                && correctWord.charAt(j) == typoWord.charAt(k + 1)) {
                            markers.append("^");
                            k++;
                        } else {
                            if (j + 1 < correctWord.length()
                                    && correctWord.charAt(j + 1) == typoWord.charAt(k)) {
                                markers.append("^");
                                j++;
                            } else {
                                if (k + 1 < typoWord.length()
                                        && correctWord.charAt(j) == typoWord.charAt(k + 1)) {
                                    markers.append("^");
                                    k++;
                                } else {
                                    markers.append("^");
                                    j++;
                                    k++;
                                    while (j < correctWord.length()
                                            && k < typoWord.length()
                                            && correctWord.charAt(j) != typoWord.charAt(k)) {
                                        j++;
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    j++;
                    k++;
                } else if (j < correctWord.length()) {
                    markers.append("^");
                    j++;
                } else {
                    markers.append("^");
                    k++;
                }
            }

            markers.append(" ");
        }
    }

    /**
     * Подсчитать количество опечаток за тренировку
     * @return количество опечаток
     */
    public int countNumberOfTypos() {
        int typoCount = 0;

        Map<String, String> marks = markTypo();

        for (String mark : marks.values()) {
            typoCount += countTyposInString(mark);
        }

        return typoCount;
    }

    /**
     * Подсчитать количество вхождений символа в строку
     * @param markedString строка с пометками опечаток
     * @return количество опечаток в строке
     */
    private int countTyposInString(String markedString) {
        int typoCountInString = 0;

        for (char c : markedString.toCharArray()) {
            if (c == '^') {
                typoCountInString++;
            }
        }
        return typoCountInString;
    }
}
