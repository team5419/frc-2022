package org.team5419.frc2022.fault.util

import java.util.LinkedList

class MovingAverageFilter(numberOfValues: Int) {
    private val values: LinkedList<Double>
    private val numberOfValues: Int
    public val average: Double
        get() = values.toDoubleArray().sum() / values.size

    init {
        this.values = LinkedList()
        for (i in 1..numberOfValues) {
            this.values.add(0.0)
        }
        this.numberOfValues = numberOfValues
    }

    fun addValue(value: Double) {
        this.values.add(value)
        this.values.remove()
    }

    operator fun plusAssign(value: Double) = addValue(value)
}
