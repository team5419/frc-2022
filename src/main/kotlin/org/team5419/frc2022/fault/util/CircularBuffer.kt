package org.team5419.frc2022.fault.util

public open class CircularBuffer<T>(maxSize: Int) {

    private val mMaxSize: Int

    public val elements: MutableList<T>

    init {
        mMaxSize = maxSize
        if (mMaxSize <= 0) {
            throw IllegalArgumentException("maxSize must be a positive integer.")
        }
        elements = mutableListOf()
    }

    public fun add(element: T): T? {
        elements.add(element)
        if (elements.size > mMaxSize) {
            val poppedValue = elements.removeAt(0)
            return poppedValue
        }
        return null
    }

    public fun clear() {
        elements.clear()
    }
}
