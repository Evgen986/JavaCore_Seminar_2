package org.example;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static final char DOT_HUMAN = 'X';  // Фишка игрока - человека
    private static final char DOT_AI = 'O';  // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*';  // Фишка пустого поля
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;  // Двумерный массив хранит состояние игрового поля
    private static int fieldSizeX;  // Размерность игрового поля
    private static int fieldSizeY;  // Размерность игрового поля
    private static int winCount;  // Количество фишек для победы
    private static int difficult;  // Уровень сложности игры.


    public static void main(String[] args) {
        while (true){
            rulesGame();
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if(gameCheck(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да) : ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Запрос у пользователя условий игры.
     */
    private static void rulesGame(){
        System.out.print("Введите размерность поля (одну цифру) : ");
        int sizeField = scanner.nextInt();
        System.out.print("Введите количество фишек для победы : ");
        int count_dot = scanner.nextInt();
        System.out.print("""
                Выберите сложность игры:
                1 - низкая сложность;
                2 - обычная сложность;
                3 - я хочу страдать;
                Введите цифру: """);
        int gameDifficulty = scanner.nextInt();
        fieldSizeX = sizeField;
        fieldSizeY = sizeField;
        winCount = count_dot;
        difficult = gameDifficulty - 1;
    }

    /**
     * Инициализация начального состояния игры.
     */
    private static void initialize() {
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовать текущее состояние игрового поля.
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeY; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeX; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
    }

    /**
     * Обработка хода игрока (человека).
     */
    private static void humanTurn(){
        int x, y;
        do {
            System.out.print("Укажите координаты хода X и Y (от 1 до 3) через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Обработка хода компьютера.
      */
    private static void aiTurn(){
        if (aiCellSearch(DOT_AI, winCount - 1)){} // Попытка победить АИ
        else if (aiCellSearch(DOT_HUMAN, winCount - difficult)) {} // Попытка предотвратить победу игрока
        else{
            int x, y;
            // Рандомный ход
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }
            while (!isCellEmpty(x, y));

            field[x][y] = DOT_AI;
        }
    }

    /**
     * Расчет клетки для хода искусственного интеллекта.
     * @param dot фишка для поиска оптимального хода
     * @param win сложность игры
     * @return true - если клетка была найдена, иначе false.
     */
    private static boolean aiCellSearch(char dot, int win){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {

                // проверка горизонтали
                if (checkHorizontal(x, y, dot, win)) {
                    // ближайшая клетка
                    if(isCellValid(x, y - 1) && isCellEmpty(x, y-1)) {
                        field[x][y - 1] = DOT_AI;
                        return true;
                    }
                    // дальняя клетка
                    else if (isCellValid(x, y + win) && isCellEmpty(x, y + win)) {
                        field[x][y + win] = DOT_AI;
                        return true;
                    }
                }

                // проверка вертикали вверх
                if (checkVerticalUp(x, y, dot, win)) {
                    // ближайшая клетка
                    if (isCellValid(x + 1, y) && isCellEmpty(x+1, y)){
                        field[x + 1][y] = DOT_AI;
                        return true;
                    }
                    // дальняя клетка
                    else if (isCellValid(x - win, y) && isCellEmpty(x - win, y)){
                        field[x - win][y] = DOT_AI;
                        return true;
                    }
                }

                // проверка вертикали вниз
                if (checkVerticalDown(x, y, dot, win)){
                    // ближайшая клетка
                    if (isCellValid(x -1, y) && isCellEmpty(x-1, y)){
                        field[x - 1][y] = DOT_AI;
                        return true;
                    }
                    // дальняя клетка
                    else if (isCellValid(x + win, y) && isCellEmpty(x + win, y)){
                        field[x + win][y] = DOT_AI;
                        return true;
                    }
                }

                // проверка диагонали вверх
                if (checkDiagonalUp(x, y, dot, win)){
                    // ближайшая клетка
                    if (isCellValid(x + 1, y - 1) && isCellEmpty(x+1, y-1)){
                        field[x+1][y - 1] = DOT_AI;
                        return true;
                    }
                    // дальняя клетка
                    else if (isCellValid(x - win, y + win) && isCellEmpty(x - win, y + win)){
                        field[x - win][y + win] = DOT_AI;
                        return true;
                    }
                }

                // проверка диагонали вниз
                if (checkDiagonalDown(x, y, dot, win)){
                    // ближайшая клетка
                    if (isCellValid(x -1, y - 1) && isCellEmpty(x - 1, y -1)){
                        field[x - 1][y - 1] = DOT_AI;
                        return true;
                    }
                    // дальняя клетка
                    else if (isCellValid(x + win, y + win) && isCellEmpty(x + win, y + win)){
                        field[x + win][y + win] = DOT_AI;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверка, является ли ячейка пустой (DOT_EMPTY).
     * @param x координата Х.
     * @param y координата Y.
     * @return true - если ячейка пуста, иначе false.
     */
    private static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода.
     * @param x координата Х.
     * @param y координата Y.
     * @return true - если ячейка доступна для ввода, иначе false.
     */
    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка состояния игры.
     * @param dot фишка игрока.
     * @param winStr сообщение о победе.
     * @return признак продолжения игры (true - завершение игры, иначе - false)
     */
    private static boolean gameCheck(char dot, String winStr){
        if (checkWin(dot)){
            System.out.println(winStr);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        return false; // Продолжаем игру
    }

    /**
     * Проверка победы.
     * @param c фишка игрока (х или О)
     * @return true - в случае победы, иначе - false.
     */
    private static boolean checkWin(char c){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (checkHorizontal(x, y, c, winCount)) return true; // горизонталь
                if (checkVerticalUp(x, y, c, winCount)) return true;  // верхняя вертикаль
                if (checkVerticalDown(x, y, c, winCount)) return true;  // нижняя вертикаль
                if (checkDiagonalUp(x, y, c, winCount)) return true;  // верхняя диагональ
                if (checkDiagonalDown(x, y, c, winCount)) return true;  // нижняя диагональ
            }
        }
        return false;
    }

    /**
     * Проверка горизонтали
     * @param x координата Х стартовой точки
     * @param y координата Y стартовой точки
     * @param dot фишка для проверки
     * @param win количество повторений подряд для победы
     * @return true - при совпадении кол-ва повторений и условия победы, иначе false.
     */
    private static boolean checkHorizontal(int x, int y, char dot, int win){
        int count = 0;  // счетчик повторений фишки
        for (int i = 0; i < win; i++) {  // Перебираем с ограничением условий победы
            if (y + i > fieldSizeY - 1) break;  // Прерываем перебор в случае выхода за пределы массива
            if (field[x][y + i] != dot) return false; // Возвращаем ложь если фишка в ячейки не соответствует проверяемой
            count++;  // увеличиваем счетчик
        }
        System.out.println("count = " + count + "result = " + (count == win));
        return count == win;  // сравниваем счетчик с условием победы
    }
    /**
     * Проверка вертикали вверх
     * @param x координата Х стартовой точки
     * @param y координата Y стартовой точки
     * @param dot фишка для проверки
     * @param win количество повторений подряд для победы
     * @return true - при совпадении кол-ва повторений и условия победы, иначе false.
     */
    private static boolean checkVerticalUp(int x, int y, char dot, int win){
        int count = 0;
        for (int i = 0; i < win; i++) {
            if (x - i < 0) break;
            if (field[x - i][y] != dot) return false;
            count ++;
        }
        return count == win;
    }
    /**
     * Проверка вертикали вниз
     * @param x координата Х стартовой точки
     * @param y координата Y стартовой точки
     * @param dot фишка для проверки
     * @param win количество повторений подряд для победы
     * @return true - при совпадении кол-ва повторений и условия победы, иначе false.
     */
    private static boolean checkVerticalDown(int x, int y, char dot, int win){
        int count = 0;
        for (int i = 0; i < win; i++) {
            if (x + i > fieldSizeX - 1) break;
            if (field[x + i][y] != dot) return false;
            count++;
        }
        return count == win;
    }
    /**
     * Проверка диагонали вниз
     * @param x координата Х стартовой точки
     * @param y координата Y стартовой точки
     * @param dot фишка для проверки
     * @param win количество повторений подряд для победы
     * @return true - при совпадении кол-ва повторений и условия победы, иначе false.
     */
    private static boolean checkDiagonalDown(int x, int y, char dot, int win){
        int count = 0;
        for (int i = 0; i < win; i++) {
            if (x + i > fieldSizeX - 1 || y + i > fieldSizeY - 1) break;
            if (field[x + i][y + i] != dot) return false;
            count++;
        }
        return count == win;
    }
    /**
     * Проверка диагонали вверх
     * @param x координата Х стартовой точки
     * @param y координата Y стартовой точки
     * @param dot фишка для проверки
     * @param win количество повторений подряд для победы
     * @return true - при совпадении кол-ва повторений и условия победы, иначе false.
     */
    private static boolean checkDiagonalUp(int x, int y, char dot, int win){
        int count = 0;
        for (int i = 0; i < win; i++) {
            if (x - i < 0 || y - i < 0) break;
            if (field[x - i][y - i] != dot) return false;
            count++;
        }
        return count == win;
    }

    /**
     * Проверка на ничью.
     * @return true - если зафиксирована ничья, иначе - false.
     */
    private static boolean checkDraw(){
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }
}
