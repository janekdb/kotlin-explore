package life

class EmptyBoxListException : Exception("Empty box list")

interface BoxPicker {
    fun pickBox(boxes: List<Box>): Box
}