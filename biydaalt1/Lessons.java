public class Lessons {
    public Subject learned;
    public int score; // түүвэр оноо (0-100)

    public Lessons(Subject learned, int score) {
        this.learned = learned;
        this.score = score;
    }

    public float toGPA() {
        if (score >= 90)
            return 4.0f;
        else if (score >= 80)
            return 3.5f;
        else if (score >= 70)
            return 3.0f;
        else if (score >= 60)
            return 2.0f;
        else
            return 0.0f; // F
    }

    @Override
    public String toString() {
        return learned.name + " (" + score + ")";
    }
}
