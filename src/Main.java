import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Main{
    // Метод для поиска самой длинной последовательности 'X', с помощью регулярных выражений
    public static int findMaxXSequence(String data) {
        // Регулярное выражение для поиска последовательностей 'X'
        Pattern pattern = Pattern.compile("X+");
        Matcher matcher = pattern.matcher(data);
        int maxLength = 0;

        // Перебор всех найденных последовательностей 'X'
        while (matcher.find()) {
            int length = matcher.end() - matcher.start();
            maxLength = Math.max(maxLength, length);
        }

        return maxLength;
    }

    public static void main(String[] args){
        // конструкция try которая сама освобождает ресурсы
        try(BufferedReader reader = new BufferedReader(new FileReader("24_demo.txt"));
            ExecutorService executor = Executors.newFixedThreadPool(5)){ // Используем ExecutorService для работы с потоками, даём 5 потоков

            // Список для хранения результатов выполнения потоков
            List<Future<Integer>> futures = new ArrayList<>();

            String line;

            // Чтение файла построчно
            while ((line = reader.readLine()) != null) {
                // Использование многопоточности для более быстрой обработки большого файла
                String finalLine = line;
                Future<Integer> future = executor.submit(() -> findMaxXSequence(finalLine));
                futures.add(future);
            }

            // Ждем завершения работы всех потоков и находим максимальное значение
            int max = 0;
            for (Future<Integer> future : futures) {
                max = Math.max(max, future.get());
            }

            System.out.println("Самая длинная последовательность X: " + max);

        } catch (IOException | InterruptedException | ExecutionException ex){
            System.out.println(ex.getMessage());
        }
    }
}