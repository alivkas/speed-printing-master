package org.example.text;

import java.util.*;

/**
 * Взаимодействие с опечатками в тексте
 */
public class Typo {

    private final Map<String, String> typos;
    private int typoCounts;

    /**
     * Конструктор Typo
     */
    public Typo() {
        this.typos = new LinkedHashMap<>();
        this.typoCounts = 0;
    }

    /**
     * Геттер typoCounts
     * @return typoCounts
     */
    public int getTypoCounts() {
        return this.typoCounts;
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
     * Полностью очистить словарь опечаток
     */
    public void clearTypo() {
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
     * сохраняя правильное местоположение
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

            while (j < correctWord.length() || k < typoWord.length()) {
                if (j < correctWord.length() && k < typoWord.length()) {
                    if (correctWord.charAt(j) == typoWord.charAt(k)) {
                        markers.append(" ");
                        j++;
                        k++;
                    } else {
                        markers.append("^");

                        int nextMatchIndex = k + 1;
                        while (nextMatchIndex < typoWord.length() &&
                                j < correctWord.length() &&
                                correctWord.charAt(j) == typoWord.charAt(nextMatchIndex)) {
                            nextMatchIndex++;
                            j++;
                        }

                        if (nextMatchIndex > k + 1) {
                            k = nextMatchIndex;
                        } else {
                            j++;
                            k++;
                        }
                    }
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

        if (splitTypo.length > minLength) {
            for (int i = minLength; i < splitTypo.length; i++) {
                markers.append(" ".repeat(splitTypo[i].length()));
                markers.append(" ");
            }
        } else if (splitCorrect.length > minLength) {
            for (int i = minLength; i < splitCorrect.length; i++) {
                markers.append(" ".repeat(splitCorrect[i].length()));
                markers.append(" ");
            }
        }
    }

    /**
     * Получить количество опечаток за тренировку
     * @return количество опечаток
     */
    public int getTypoCount() {
        Map<String, String> marks = markTypo();

        for (String mark : marks.values()) {
            typoCounts += getTypoCountInOneString(mark);
        }

        return typoCounts;
    }

    /**
     * Подсчитать количество вхождений символа в строку
     * @param markedString строка с пометками опечаток
     * @return количество опечаток в строке
     */
    private int getTypoCountInOneString(String markedString) {
        int count = 0;

        for (char c : markedString.toCharArray()) {
            if (c == '^') {
                count++;
            }
        }
        return count;
    }

    /**
     * Обнулить счетчик
     */
    public void clearTypoCount() {
        typoCounts = 0;
    }
}