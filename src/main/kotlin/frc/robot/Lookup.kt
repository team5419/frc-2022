data class LookupEntry(val distance: Double, val velocity: Double) {
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        add(0.5, 2000.0)
        add(1.0, 2100.0)
        add(5.0, 3000.0)
        add(10.0, 6000.0)
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

    fun get(distance: Double): LookupEntry? {
        for (i in 0..table.size-1) {
            val entry = table.get(i)

            val prevEntery = table.get(i - 1)

            val percent = (distance - entry.distance) / (prevEntery.distance - entry.distance)

            val invertedPercent = percent - 1.0

            if (entry.distance < distance) {
                if (i == 0) {
                    println("Distance is too large.")
                    return null
                }

                return LookupEntry(distance, entry.velocity * percent + prevEntery.velocity * invertedPercent)
            }
        }

        println("Distance is too small.")
        return null
    }
}
