import org.team5419.frc2022.subsystems.ShotSetpoint

data class LookupEntry(
    val distance: Double,
    override val angle: Double,
    override val velocity: Double
) : ShotSetpoint {
    override fun toString() = "D: ${distance}, A: ${angle}, V: ${velocity}"
}

object Lookup {

    private val table: MutableList<LookupEntry>

    init {
        table = mutableListOf<LookupEntry>()
        println("add 0")
        //add(10.0, 12.0, 3500.0 )
        add( 2.73, 12.0, 3500.0 )
        println("add 1")
        add( 2.12, 12.0, 3500.0 )
        add( 1.56, 12.0, 3500.0 )
        add( 0.97, 14.0, 4000.0 )
        add( 0.64, 15.0, 4500.0 )
        add( 0.31, 14.8, 4800.0 )
        println("init")
    }

    // largest entery at 0

    fun add(distance: Double, angle: Double, velocity: Double) {
        println("size" + table.size)

        if (table.size == 0) {
            table.add(LookupEntry(distance, angle, velocity))
            return
        }

        for (i in 0..table.size-1) {
            println(i)
            val entry = table.get(i)

            if (entry.distance < distance) {
                table.add(i, LookupEntry(distance, angle, velocity))
                return
            }
        }

        table.add(LookupEntry(distance, angle, velocity))
    }

    fun get(distance: Double): LookupEntry? {
        println("Distence" + distance)
        println("table size is " + table.size)
        for (i in 0..table.size-1) {
            val entry = table.get(i)
            println(entry.distance.toString() + " is the entry distance for " + i)

            if (entry.distance < distance) {
                println(entry.distance)
                if (i == 0) {
                    println("distance is too large")
                    return null
                }

                val prevEntery = table.get(i - 1)

                val percent = (distance - entry.distance) / (prevEntery.distance - entry.distance)

                val invertedPercent = 1.0 - percent

                return LookupEntry(
                    distance,
                    entry.angle * percent + prevEntery.angle * invertedPercent,
                    entry.velocity * percent + prevEntery.velocity * invertedPercent
                )
            }
        }

        println("distance is too small")
        return null
    }
}
