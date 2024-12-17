package org.example.text;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import java.util.*;

/**
 * Взаимодействие с опечатками в тексте
 */
public class Typo {
    private final Map<String, String> typos;
    private static final int OPERATIONS = 2;

    /**
     * Конструктор Typo, который инициализирует поле typos
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
    public List<String> markTypo() {
        List<String> marks = new ArrayList<>(typos.size());

        for (Map.Entry<String, String> entry : typos.entrySet()) {
            String correct = entry.getKey();
            String typo = entry.getValue();

            StringBuilder markers = new StringBuilder();

            String[] splitCorrect = correct.split(" ");
            String[] splitTypo = typo.split(" ");

            markupsAlgorithm(splitCorrect, splitTypo, markers);
            marks.add(markers.toString());
        }

        return Collections.unmodifiableList(marks);
    }

    /**
     * Производит процесс создания пометок на словах с опечатками,
     * отмечая плюсами добавленные символы и минусами удаленные
     */
    private void markupsAlgorithm(String[] splitCorrect, String[] splitTypo, StringBuilder markers) {
        int minLength = Math.min(splitCorrect.length, splitTypo.length);

        for (int i = 0; i < minLength; i++) {
            DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
            List<DiffMatchPatch.Diff> diffs = diffMatchPatch.diffMain(splitCorrect[i], splitTypo[i]);

            for (DiffMatchPatch.Diff diff : diffs) {
                switch (diff.operation) {
                    case EQUAL -> markers.append(diff.text);
                    case INSERT -> markers.append("+").append(diff.text).append("+");
                    case DELETE ->  markers.append("-").append(diff.text).append("-");
                }
            }
        }
    }

    /**
     * Подсчитать количество опечаток за тренировку
     * @return количество опечаток
     */
    public int countNumberOfTypos() {
        StringBuilder textConcatenation = new StringBuilder();
        List<String> marks = markTypo();

        for (String mark : marks) {
            textConcatenation.append(mark).append("\n");
        }
        return countOperations(textConcatenation.toString());
    }

    /**
     * Считать количество операций (добавления, удаления, замены) над символами в тексте
     * @param text текст
     * @return количество ошибок
     */
    private int countOperations(String text) {
        int operationCount = 0;
        boolean isDeletionSequence = false;

        if (typos.isEmpty()) {
            return operationCount;
        }

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);

            if (currentChar == '-') {
                if (i > 0 && text.charAt(i - 1) != '-') {
                    if (!isDeletionSequence) {
                        operationCount++;
                    }
                    isDeletionSequence = true;
                }
            } else if (currentChar == '+') {
                if (i > 0 && text.charAt(i - 1) != '+') {
                    if (!isDeletionSequence) {
                        operationCount++;
                    }
                }
                isDeletionSequence = false;
            } else {
                isDeletionSequence = false;
            }
        }

        int result = operationCount == OPERATIONS
                ? operationCount / OPERATIONS
                : operationCount / OPERATIONS - 1;
        return result == -1
                ? 0
                : result;
    }
}
