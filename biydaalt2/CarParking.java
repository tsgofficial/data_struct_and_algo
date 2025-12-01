import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Машины зогсоолын програм
// Оролтын файл: cars.txt
// Зорилго: Стек ашиглан 10 машины багтаамжтай зогсоолыг симуляц хийх

public class CarParking {

    // Зогсоолын багтаамж
    private static final int CAPACITY = 10;

    // ------------------------
    // Car өгөгдлийн төрөл
    // ------------------------
    static class Car {
        private String number; // машины дугаар

        public Car(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return number;
        }
    }

    // Оролтын мөрийг дотоод бүтэц болгон хадгалах класс
    static class Command {
        char type; // 'A' - Arrival, 'D' - Departure
        Car car;

        public Command(char type, Car car) {
            this.type = type;
            this.car = car;
        }
    }

    // ------------------------
    // main функц
    // ------------------------
    public static void main(String[] args) {
        // 1. Оролтыг унших
        List<Command> commands = input("cars.txt");

        // 2. Мэдээллийг боловсруулах (стекийн логик)
        List<String> results = process(commands);

        // 3. Гаралтыг хэвлэх
        output(results);
    }

    // ------------------------
    // 1. input() - оролт
    // cars.txt файлыг уншаад Command жагсаалт үүсгэнэ
    // ------------------------
    public static List<Command> input(String fileName) {
        List<Command> commands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Хоосон мөр алгасах
                if (line.isEmpty())
                    continue;

                // Мөрийг таслаж авах (A AR48-50 гэх мэт)
                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    // Жишээ файл доторх "2" гэх мэт буруу мөрүүдийг алгасах
                    continue;
                }

                char type = parts[0].charAt(0); // 'A' эсвэл 'D'
                String plate = parts[1]; // машины дугаар

                if (type == 'A' || type == 'D') {
                    commands.add(new Command(type, new Car(plate)));
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }

        return commands;
    }

    // ------------------------
    // 2. process() - боловсруулалт
    // Стек ашиглан машины ирэлт/гаралтыг тооцоолно
    // ------------------------
    public static List<String> process(List<Command> commands) {
        List<String> outputs = new ArrayList<>();

        // Үндсэн стек: зогсоолд байгаа машинууд
        Stack<Car> garage = new Stack<>();

        for (Command cmd : commands) {
            char type = cmd.type;
            Car car = cmd.car;

            if (type == 'A') {
                // Машин ирж байна
                if (garage.size() < CAPACITY) {
                    garage.push(car); // стекийн орой дээр тавина
                    outputs.add("Arrival " + car.getNumber() + " -> There is room.");
                } else {
                    // Зогсоол дүүрэн
                    outputs.add("Arrival " + car.getNumber() + " -> Garage full, this car cannot enter.");
                }

            } else if (type == 'D') {
                // Машин гарах гэж байна
                if (garage.isEmpty()) {
                    // Ямар ч машин байхгүй
                    outputs.add("Departure " + car.getNumber() + " -> This car not in the garage.");
                    continue;
                }

                // Түр стек: гаргаж түр гаргасан машинууд
                Stack<Car> temp = new Stack<>();
                boolean found = false;

                // Зогсоолын амаас (стек дээдээс) хайна
                while (!garage.isEmpty()) {
                    Car top = garage.pop();
                    if (top.getNumber().equals(car.getNumber())) {
                        // Хайж байсан машин олдлоо
                        found = true;
                        break;
                    } else {
                        // Түр гарсан машинуудыг temp стект хадгална
                        temp.push(top);
                    }
                }

                if (found) {
                    int moved = temp.size(); // хэдэн машин түр гарсан тоо

                    // Түр гаргасан машинуудыг буцааж анхны дарааллаар нь оруулна
                    while (!temp.isEmpty()) {
                        garage.push(temp.pop());
                    }

                    outputs.add("Departure " + car.getNumber() + " -> " + moved + " cars moved out.");
                } else {
                    // Ийм машин олдоогүй - бүх машиныг буцааж оруулна
                    while (!temp.isEmpty()) {
                        garage.push(temp.pop());
                    }

                    outputs.add("Departure " + car.getNumber() + " -> This car not in the garage.");
                }
            }
        }

        return outputs;
    }

    // ------------------------
    // 3. output() - гаралт
    // Бэлтгэсэн мөрүүдийг дэлгэцэнд хэвлэнэ
    // ------------------------
    public static void output(List<String> outputs) {
        for (String line : outputs) {
            System.out.println(line);
        }
    }
}
