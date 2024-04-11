package life

import kotlin.random.Random

class RandomBoxPicker : BoxPicker {
    private val random = Random(System.currentTimeMillis())

    /**
     * @return Any box from the list with boxes at lower indexes being
     * more likely to be picked. If the list was a list of square boxes
     * of different sizes sorted in descending order by size then larger
     * boxes are individually more likely to be returned than smaller boxes
     */
    override fun pickBox(boxes: List<Box>): Box {
        if (boxes.isEmpty()) throw EmptyBoxListException()
        val randomIndex = ((random.nextFloat() * random.nextFloat()) * boxes.size).toInt()
        return boxes[randomIndex]
    }
}
