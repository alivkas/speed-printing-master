package org.example.training;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Управление сессией тренировки
 */
public class TrainingSession {
    private final int lasting; // Длительность тренировки
    private Timer timer;
    private boolean session; // Статус активности сессии

    /**
     * Конструктор, который передает ссылки на объекты lasting и session
     * @param lasting длительность тренировки в миллисекундах
     */
    public TrainingSession(int lasting){
        this.lasting = lasting;
        this.session = false; // Изначально не активна
    }

    /**
     * Запуск сессии тренировки с установленным временем
     */
    public void start(){
        session = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session = false;
                System.out.println("Тренировка завершена");
                timer.cancel();
            }
        }, lasting);
        System.out.println("Новая тренировка на " + lasting/60000  + " минут");
    }

    /**
     * Остановка текущей сессии
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            session = false;
            System.out.println("Тренировка остановлена");
        }
    }

    /**
     * Проверка активности сессии
     * @return true, если сессия активна, иначе false
     */
    public boolean isActive() {
        return session;
    }
}