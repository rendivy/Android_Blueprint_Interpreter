package Block

import Instruction
import java.io.Serializable

open class ViewBlock(
    val instruction: Instruction,
    var expression: String = "",
    var breakpoint: Boolean = false,
    var parsed: List<String> = listOf()
) : Serializable

