package com.example.android_blueprint.model


sealed class BlockValue(
    val text: String,
) {
    enum class BinaryOperator(val text: String) {
        SUBTRACTION("-"),
        MULTIPLICATION("×"),
        DIVISION("÷"),
        REMAINDER("%"),
        ADDITION("+"),
        EQUALITY("=="),
        NOT_EQUAL("!="),
        GREATER(">"),
        LESS("<"),
        GREATER_OR_EQUAL(">="),
        LESS_OR_EQUAL("<="),
        LOG("log"),
        POW("pow")
    }

    enum class UnaryOperator(val text: String) {
        INVERSION("not"),
        ABS("abs"),
        SIN("sin"),
        COS("cos"),
        TG("tg"),
        CTG("ctg"),
        ARCSIN("arcsin"),
        ARCCOS("arccos"),
        ARCTG("arctg"),
        ARCCTG("arcctg")
    }

    object InitializationBlock : BlockValue(
        text = "Initialization",
    )

    object SetBlock : BlockValue(
        text = "Set",
    )

    object IfBlock : BlockValue(
        text = "If",
    )

    object EndifBlock : BlockValue(
        text = "Endif",
    )

    object PrintBlock : BlockValue(
        text = "Print",
    )

    object StartBlock : BlockValue(
        text = "Start",
    )

    object EndBlock : BlockValue(
        text = "End",
    )

    object ForBlock : BlockValue(
        text = "For",
    )

    object WhileBlock : BlockValue(
        text = "While",
    )

    object GetValueBlock : BlockValue(
        text = "Get value",

        )

    object FunctionBlock : BlockValue(
        text = "Function",
    )

    object BreakBlock : BlockValue(
        text = "Break",
    )

    object ContinueBlock : BlockValue(
        text = "Continue",
    )


    object ReturnBlock : BlockValue(
        text = "Return",
    )

    object CallFunctionBlock: BlockValue(
        text = "Call function"
    )

}
