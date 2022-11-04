package frc.robot;
import frc.robot.classes.RGB;

data class LookupEntry(val distance: Double, val mainVelocity: Double, val kickerVelocity: Double, val color: RGB) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        table.add(LookupEntry(1.7, 14270.0, 15307.0, RGB(255, 247, 0)))
        table.add(LookupEntry(1.8, 14200.0, 15350.0, RGB(255, 247, 0)))
        table.add(LookupEntry(1.85, 14143.0, 15406.0, RGB(255, 247, 0)))
        table.add(LookupEntry(1.9, 13891.0, 15027.0, RGB(255, 247, 0)))
        table.add(LookupEntry(2.0, 15000.0, 15000.0, RGB(255, 50, 0))) // original
        table.add(LookupEntry(2.1, 14901.0, 15027.0, RGB(255, 50, 0)))
        table.add(LookupEntry(2.2, 14901.0, 15154.0, RGB(255, 50, 0)))
        table.add(LookupEntry(2.3, 15100.0, 15200.0, RGB(255, 50, 0)))
        table.add(LookupEntry(2.4, 15406.0, 15406.0, RGB(255, 50, 0)))
        table.add(LookupEntry(2.51, 17805.0, 18338.0, RGB(255, 0, 0)))
        table.add(LookupEntry(2.63, 17174.0, 17427.0, RGB(255, 0, 0)))
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
