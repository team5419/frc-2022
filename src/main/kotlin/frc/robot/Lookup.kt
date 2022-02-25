package frc.robot;

data class LookupEntry(val distance: Double, val mainVelocity: Double, val kickerVelocity: Double) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        add(1.68, 13000.0, 16900.0)
        add(2.55, 20000.0, 16000.0)
    }

    fun add(distance: Double, mainVelocity: Double, kickerVelocity: Double) {
        if (table.size == 0) {
            table.add(LookupEntry(distance, mainVelocity, kickerVelocity))
            return
        }

        for (i in 0..table.size-1) {
            val entry = table.get(i)

            if (entry.distance < distance) {
                table.add(i, LookupEntry(distance, mainVelocity, kickerVelocity))
                break
            }
        }
    }

    fun get(distance: Double): LookupEntry {
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
