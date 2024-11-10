package org.example.animation;

/**
 * Анимация в консоли
 */
public class Animation {

    private final int count = 3;

    /**
     * Обратный отсчет до начала тренировки
     */
    public void countingDown() {
        for (int i = count; i >= 1; i--) {
            System.out.println(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Поток занят", e);
            }
        }
    }
}
