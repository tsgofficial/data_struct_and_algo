import java.io.*;
import java.util.*;

public class Registration {
    public ArrayList<Student> studentList = new ArrayList<>();
    public ArrayList<Subject> subjectList = new ArrayList<>();
    public ArrayList<Major> majorList = new ArrayList<>();

    public void loadSubjects(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] values = line.split("/");
            if (values.length != 3) {
                System.out.println("Анхаар: Файлын формат буруу: " + fileName + " мөр " + lineNumber + ": " + line);
                continue;
            }
            Subject s = new Subject(values[0], values[1], Float.parseFloat(values[2]));
            subjectList.add(s);
        }
        br.close();
    }

    public void loadMajors(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] values = line.split("/");
            if (values.length != 2) {
                System.out.println("Анхаар: Файлын формат буруу: " + fileName + " мөр " + lineNumber + ": " + line);
                continue;
            }
            Major m = new Major(values[0], values[1]);
            majorList.add(m);
        }
        br.close();
    }

    public void loadExams(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] values = line.split("/");
            if (values.length != 3) {
                System.out.println("Анхаар: Файлын формат буруу: " + fileName + " мөр " + lineNumber + ": " + line);
                continue;
            }

            String studentCode = values[0];
            String subjectCode = values[1];
            int score = Integer.parseInt(values[2]);

            Subject subj = findSubject(subjectCode);
            if (subj == null) {
                System.out.println("Анхаар: Хичээл олдсонгүй: " + subjectCode);
                continue;
            }

            Student st = findStudent(studentCode);
            if (st == null) {
                st = new Student(studentCode);
                studentList.add(st);
            }

            st.addLesson(new Lessons(subj, score));
        }
        br.close();
    }

    private Subject findSubject(String code) {
        for (Subject s : subjectList)
            if (s.code.equals(code))
                return s;
        return null;
    }

    private Student findStudent(String code) {
        for (Student s : studentList)
            if (s.code.equals(code))
                return s;
        return null;
    }

    public void listSubjects() {
        System.out.println("\n=== Бүх хичээлүүд ===");
        for (Subject s : subjectList)
            System.out.println(s);
    }

    public void listMajors() {
        System.out.println("\n=== Бүх мэргэжлүүд ===");
        for (Major m : majorList)
            System.out.println(m);
    }

    public void averageGPA() {
        if (studentList.isEmpty()) {
            System.out.println("\nОюутнууд олдсонгүй!");
            return;
        }

        float total = 0;
        for (Student s : studentList)
            total += s.GPA;
        float avgGPA = total / studentList.size();
        System.out.println("\nДундаж GPA: " + String.format("%.2f", avgGPA));
    }

    public void listDismissedStudents() {
        System.out.println("\n=== 3-аас дээш F авсан оюутнууд ===");
        for (Student s : studentList) {
            if (s.countFails() >= 3)
                System.out.println(s.code + " GPA=" + s.GPA);
        }
    }

    public void listScoresBySubject() {
        for (Subject subj : subjectList) {
            System.out.println("\n=== " + subj.name + " ===");
            for (Student s : studentList) {
                for (Lessons l : s.lessons) {
                    if (l.learned.code.equals(subj.code)) {
                        System.out.println(s.code + " -> " + l.score);
                    }
                }
            }
        }
    }

    public void listScoresByMajor() {
        for (Major major : majorList) {
            System.out.println("\n=== " + major.name + " ===");
            for (Student s : studentList) {
                if (s.code.startsWith(major.code)) {
                    for (Lessons l : s.lessons) {
                        System.out.println(s.code + " -> " + l.learned.name + ": " + l.score);
                    }
                    System.out.println(s);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Registration reg = new Registration();

        try {
            reg.loadSubjects("Subjects.txt");
            reg.loadMajors("Professions.txt");
            reg.loadExams("Exams.txt");
            System.out.println("✅ Өгөгдөл амжилттай ачаалагдлаа!\n");
        } catch (Exception e) {
            System.out.println("⚠️ Файлуудыг ачаалахад алдаа: " + e.getMessage());
        }

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n===== Цэс =====");
            System.out.println("1. Бүх хичээлүүдийг харуулах");
            System.out.println("2. Бүх мэргэжлүүдийг харуулах");
            System.out.println("3. Дундаж GPA-г үзүүлэх");
            System.out.println("4. Хасагдсан оюутнуудыг жагсаах (3-аас дээш F)");
            System.out.println("5. Хичээлээр оноог жагсаах");
            System.out.println("6. Мэргэжлээр оноог жагсаах");
            System.out.println("0. Гарах");
            System.out.print("Сонголт оруулах: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Буруу оруулалт. Дахин оролдоно уу.");
                continue;
            }

            switch (choice) {
                case 1 -> reg.listSubjects();
                case 2 -> reg.listMajors();
                case 3 -> reg.averageGPA();
                case 4 -> reg.listDismissedStudents();
                case 5 -> reg.listScoresBySubject();
                case 6 -> reg.listScoresByMajor();
                case 0 -> System.out.println("Баяртай!");
                default -> System.out.println("Буруу сонголт!");
            }
        }

        sc.close();
    }
}
