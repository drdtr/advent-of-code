package advent_of_code_2017.day22

enum class GridStepDirection(val dx: Int, val dy: Int) {
    Up(dx = 0, dy = 1),
    Down(dx = 0, dy = -1),
    Left(dx = -1, dy = 0),
    Right(dx = 1, dy = 0);

    fun afterLeftTurn() = when (this) {
        Up    -> Left
        Left  -> Down
        Down  -> Right
        Right -> Up
    }

    fun afterRightTurn() = when (this) {
        Up    -> Right
        Right -> Down
        Down  -> Left
        Left  -> Up
    }

    fun afterTurnaround() = when (this) {
        Up    -> Down
        Down  -> Up
        Right -> Left
        Left  -> Right
    }
}