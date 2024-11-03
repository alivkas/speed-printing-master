package org.example.animation;

/**
 * Анимация в консоли
 */
public class Animation {

    /**
     * Обратный отсчет до начала тренировки
     */
    public void countingDown() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
