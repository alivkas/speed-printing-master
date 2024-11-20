package org.example.database;


import org.example.service.UserTrainingRepository;

/**
 * Класс `ResultSave` отвечает за сохранение результатов тренировок в базу данных
 */
public class ResultSave {
    private final DatabaseManager databaseManager;
    private final UserTrainingRepository trainingSessionService;


    /**
     * Конструктор класса `ResultSave`. Создает экземпляры DatabaseManagerk UserTrainingRepository
     */
    public ResultSave() {
        this.databaseManager = new DatabaseManager();
        this.trainingSessionService = new UserTrainingRepository(databaseManager);
    }

    /**
     * Обновляет данные о тренировке в базе данных для указанного пользователя
     *
     * @param username     Имя пользователя
     * @param time         Время тренировки
     * @param averageTime  Среднее время тренировки
     */
    public void updateTrainingData(String username, int time, double averageTime) {
        trainingSessionService.addNewTrainingSession(username, time, averageTime);
    }
}
