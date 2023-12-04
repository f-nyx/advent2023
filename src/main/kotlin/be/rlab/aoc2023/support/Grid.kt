package be.rlab.aoc2023.support

data class Point(
    val x: Int,
    val y: Int,
    val value: Char
) {
    fun translateToIndex(width: Int): Int {
        return y * width + x
    }

    fun moveX(distance: Int): Point = copy(
        x = x + distance
    )

    fun moveY(distance: Int): Point = copy(
        y = y + distance
    )

    fun move(
        distanceX: Int,
        distanceY: Int
    ): Point = copy(
        x = x + distanceX,
        y = y + distanceY
    )
}

data class Grid(
    val height: Int,
    val width: Int,
    val points: List<Point>
) {
    fun pointAt(x: Int, y: Int): Point? {
        val index = y * width + x
        return if (index >= 0 && index < points.size) {
            points[index]
        } else {
            null
        }
    }

    fun neighbors(
        point: Point,
        includeVertices: Boolean = true
    ): List<Point> {
        val vertices = if (includeVertices) {
            listOfNotNull(
                pointAt(point.x + 1, point.y - 1),
                pointAt(point.x + 1, point.y + 1),
                pointAt(point.x - 1, point.y - 1),
                pointAt(point.x - 1, point.y + 1)
            )
        } else {
            emptyList()
        }
        val edges = listOfNotNull(
            pointAt(point.x + 1, point.y),
            pointAt(point.x - 1, point.y),
            pointAt(point.x, point.y + 1),
            pointAt(point.x, point.y - 1)
        )

        return (vertices + edges).filter(::inBounds)
    }

    private fun inBounds(point: Point): Boolean {
        return (point.x in 0 until width) && (point.y in 0 until height)
    }
}
