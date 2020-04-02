package evironment.antGame;

import core.State;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class AntStateOriginal implements State {
    private final int currentFood;
    private final int row;
    private final int col;
    private final CellType type;
    private final int smell;
    private final int food;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AntStateOriginal that = (AntStateOriginal) o;
        return currentFood == that.currentFood &&
                row == that.row &&
                col == that.col &&
                smell == that.smell &&
                type == that.type &&
                food == that.food;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentFood, row, col, type, smell, food);
    }

    @Override
    public String toString() {
        return "AntStateOriginal{" +
                "currentFood=" + currentFood +
                ", row=" + row +
                ", col=" + col +
                ", type=" + type +
                ", smell=" + smell +
                ", food=" + food +
                '}';
    }
}
