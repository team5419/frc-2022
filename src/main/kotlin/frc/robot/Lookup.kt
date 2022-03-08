package frc.robot;

data class LookupEntry(val distance: Double, val mainVelocity: Double, val kickerVelocity: Double) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        add(1.65, 13000.0, 16000.0) // 13000, 16900
        add(2.0, 15000.0, 15000.0) // 17000, 16000
        add(3.0, 18000.0, 16000.0)
    }

    fun add(distance: Double, mainVelocity: Double, kickerVelocity: Double) {
        if (table.size == 0) {
            table.add(LookupEntry(distance, mainVelocity, kickerVelocity))
            return
        }

        for (i in 0..table.size-1) {
            val entry = table.get(i)

            if (entry.distance > distance) {
                table.add(i, LookupEntry(distance, mainVelocity, kickerVelocity))
                break
            }
        }

        table.add(table.size, LookupEntry(distance, mainVelocity, kickerVelocity))
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
