package Entities.Enums;

public enum LetterGrades {
    A,
    A_MINUS,
    B_PLUS,
    B,
    B_MINUS,
    C_PLUS,
    C,
    C_MINUS,
    D_PLUS,
    D,
    F;

    @Override
    public String toString() {
        switch (this) {
            case A_MINUS: return "A-";
            case B_PLUS: return "B+";
            case B_MINUS: return "B-";
            case C_PLUS: return "C+";
            case C_MINUS: return "C-";
            case D_PLUS: return "D+";
            default: return name();
        }
    }
}