package memory

import Instruction

class Variable(val name: String = "") : MemoryArea(Instruction.VAR)