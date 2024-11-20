package org.example.animation;

import org.example.interfaces.InputOutput;

/**
 * Анимация в консоли
 */
public class Animation {
    private final InputOutput inputOutput;
    private final int COUNT = 3;

    /**
     * Конструктор, который передает ссылку на реализацию InputOutput
     * @param inputOutput реализация InputOutput
     */
    public Animation(InputOutput inputOutput) {
        this.inputOutput = inputOutput;
    }

    /**
     * Обратный отсчет до начала тренировки
     */
    public void countingDown() {
        for (int i = COUNT; i >= 1; i--) {
            inputOutput.output(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Поток занят", e);
            }
        }
    }
}
