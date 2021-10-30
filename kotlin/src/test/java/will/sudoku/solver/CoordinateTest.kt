package will.sudoku.solver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CoordinateTest {

    @Test
    fun testCoordGroup() {

        assertThat(CoordinateGroup.verticalOf(Coordinate(0, 0)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0),
                Coordinate(6, 0), Coordinate(7, 0), Coordinate(8, 0)
            )
        )

        assertThat(CoordinateGroup.verticalOf(Coordinate(5, 0)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                Coordinate(3, 0), Coordinate(4, 0), Coordinate(5, 0),
                Coordinate(6, 0), Coordinate(7, 0), Coordinate(8, 0)
            )
        )

        assertThat(CoordinateGroup.verticalOf(Coordinate(8, 8)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 8), Coordinate(1, 8), Coordinate(2, 8),
                Coordinate(3, 8), Coordinate(4, 8), Coordinate(5, 8),
                Coordinate(6, 8), Coordinate(7, 8), Coordinate(8, 8)
            )
        )

        assertThat(CoordinateGroup.horizontalOf(Coordinate(0, 0)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2),
                Coordinate(0, 3), Coordinate(0, 4), Coordinate(0, 5),
                Coordinate(0, 6), Coordinate(0, 7), Coordinate(0, 8)
            )
        )
        assertThat(CoordinateGroup.horizontalOf(Coordinate(0, 5)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2),
                Coordinate(0, 3), Coordinate(0, 4), Coordinate(0, 5),
                Coordinate(0, 6), Coordinate(0, 7), Coordinate(0, 8)
            )
        )
        assertThat(CoordinateGroup.horizontalOf(Coordinate(8, 8)).coordinates).isEqualTo(
            listOf(
                Coordinate(8, 0), Coordinate(8, 1), Coordinate(8, 2),
                Coordinate(8, 3), Coordinate(8, 4), Coordinate(8, 5),
                Coordinate(8, 6), Coordinate(8, 7), Coordinate(8, 8)
            )
        )

        assertThat(CoordinateGroup.regionOf(Coordinate(0, 0)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2),
                Coordinate(1, 0), Coordinate(1, 1), Coordinate(1, 2),
                Coordinate(2, 0), Coordinate(2, 1), Coordinate(2, 2)
            )
        )
        assertThat(CoordinateGroup.regionOf(Coordinate(1, 1)).coordinates).isEqualTo(
            listOf(
                Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2),
                Coordinate(1, 0), Coordinate(1, 1), Coordinate(1, 2),
                Coordinate(2, 0), Coordinate(2, 1), Coordinate(2, 2)
            )
        )
        assertThat(CoordinateGroup.regionOf(Coordinate(5, 5)).coordinates).isEqualTo(
            listOf(
                Coordinate(3, 3), Coordinate(3, 4), Coordinate(3, 5),
                Coordinate(4, 3), Coordinate(4, 4), Coordinate(4, 5),
                Coordinate(5, 3), Coordinate(5, 4), Coordinate(5, 5)
            )
        )


    }

}
