package frc.robot;
import frc.robot.classes.RGB;

data class LookupEntry(val distance: Double, val mainVelocity: Double, val kickerVelocity: Double, val color: RGB) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        //table.add(LookupEntry(1.65, 12000.0, 13500.0, RGB(255, 0, 0)))
        table.add(LookupEntry(2.0, 14500.0, 14500.0, RGB(0, 0, 255)))
        table.add(LookupEntry(4.0, 19000.0, 19000.0, RGB(255, 0, 0)))
        // table.add(LookupEntry(6.6, 20000.0, 20000.0, RGB(100, 100, 100)))
        //table.add(LookupEntry(3.0, 17000.0, 15500.0, RGB(0, 0, 255)))
        // add(1.65, 13000.0, 16000.0, RGB(255, 0, 0)) // 13000, 16900
        // add(2.0, 15000.0, 15000.0, RGB(0, 255, 0)) // 17000, 16000
        // add(3.0, 17500.0, 15500.0, RGB(0, 0, 255))
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
                return if (Math.abs(entry.distance - distance) > Math.abs(prevEntry.distance - distance)) prevEntry else entry
            }
        }

        return table.get(table.size - 1)
    }
}
