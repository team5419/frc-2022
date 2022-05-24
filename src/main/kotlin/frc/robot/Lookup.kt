package frc.robot;
import frc.robot.classes.RGB;

data class LookupEntry(val distance: Double, val mainVelocity: Double, val kickerVelocity: Double, val color: RGB) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        table.add(LookupEntry(2.0, 15000.0, 15000.0, RGB(0, 0, 255)))
        //table.add(LookupEntry(4.0, 19000.0, 19000.0, RGB(255, 0, 0)))
    }

    fun add(distance: Double, mainVelocity: Double, kickerVelocity: Double, color: RGB) {
        if (table.size == 0) {
            table.add(LookupEntry(distance, mainVelocity, kickerVelocity, color))
            return
        }

        for (i in 0..table.size-1) {
            val entry = table.get(i)

            if (entry.distance > distance) {
                table.add(i, LookupEntry(distance, mainVelocity, kickerVelocity, color))
                return
            }
        }

        table.add(table.size, LookupEntry(distance, mainVelocity, kickerVelocity, color))
    }

    fun getClosest(distance: Double): LookupEntry {
        for (i in 0..table.size-1) {
            val entry = table.get(i)

            if (entry.distance > distance) {
                if (i == 0) {
                    return entry
                }
                val prevEntry = table.get(i - 1)
                return if (Math.abs(entry.distance - distance) + 0.5 > Math.abs(prevEntry.distance - distance)) prevEntry else entry
            }
        }

        return table.get(table.size - 1)
    }
}
