data class LookupEntry(val distance: Double, val velocity: Double) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        add(0.5, 0.0)
        add(1.0, 1.0)
        add(5.0, 2.0)
    }

    fun add(distance: Double, velocity: Double) {
        if (table.size == 0) {
            table.add(LookupEntry(distance, velocity))
            return
        }

        for (i in 0..table.size-1) {
            val entry = table.get(i)

            if (entry.distance < distance) {
                table.add(i, LookupEntry(distance, velocity))
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
