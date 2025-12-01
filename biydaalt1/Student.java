import java.util.ArrayList;

public class Student {
    public String code;
    public float GPA;
    public ArrayList<Lessons> lessons;

    public Student(String code) {
        this.code = code;
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lessons lesson) {
        lessons.add(lesson);
        recalcGPA();
    }

    private void recalcGPA() {
        if (lessons.isEmpty()) {
            GPA = 0;
            return;
        }
        
        float totalGradePoints = 0;
        float totalCredits = 0;
        
        for (Lessons l : lessons) {
            float gradePoints = l.toGPA() * l.learned.credit;
            totalGradePoints += gradePoints;
            totalCredits += l.learned.credit;
        }
        
        if (totalCredits > 0) {
            GPA = totalGradePoints / totalCredits;
        } else {
            GPA = 0;
        }
    }

    public int countFails() {
        int fails = 0;
        for (Lessons l : lessons) {
            if (l.toGPA() == 0.0f)
                fails++;
        }
        return fails;
    }

    @Override
    public String toString() {
        return code + " GPA=" + String.format("%.2f", GPA);
    }
}
