package memory

import Instruction
import java.io.Serializable

open class MemoryArea(
    val instruction: Instruction,
) : Serializable

