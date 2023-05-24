import block.*
import interpretator.Interpret

fun main() {
    val startBlock = StartBlock()
    val endBlock = EndBlock()

    val forBlock1 = ForBlock()
    forBlock1.setUserInput("i=0,i<10,i+=1")
    forBlock1.setPreviousMainFlowBlock(startBlock)

    val initializationVariableBlock1 = InitializationVariableBlock()
    initializationVariableBlock1.setUserInput("n=10")
    initializationVariableBlock1.setPreviousMainFlowBlock(forBlock1, true)

    val getVariableBlock = GetVariableBlock()
    getVariableBlock.setUserInput("n+1")

    val printBlock1 = PrintBlock()
    printBlock1.setOperator(getVariableBlock)
    printBlock1.setPreviousMainFlowBlock(initializationVariableBlock1)

    endBlock.setPreviousMainFlowBlock(forBlock1)

    val interpret = Interpret(BlockEntity.getBlocks())
    interpret.run(startBlock)
}

/*
    val startBlock = StartBlock()
    val endBlock = EndBlock()

    val initializationVariableBlock1 = InitializationAndSetVariableBlock()
    initializationVariableBlock1.setRawInput("i=10")
    startBlock.setNextMainFlowBlock(initializationVariableBlock1)
    initializationVariableBlock1.setPreviousMainFlowBlock(startBlock)

    val printBlock1 = PrintBlock()
    printBlock1.setPreviousMainFlowBlock(initializationVariableBlock1)
    initializationVariableBlock1.setNextMainFlowBlock(printBlock1)
    printBlock1.setNextMainFlowBlock(endBlock)
    endBlock.setPreviousMainFlowBlock(printBlock1)

    val getVariableBlock1 = GetVariableBlock()
    getVariableBlock1.setRawInput("print(i)+print(10+print(10))")
    printBlock1.setOperator(getVariableBlock1)

//function
    val functionBlock = FunctionBlock()
    functionBlock.setRawInput("print(n)")

    val getVariableBlock2 = GetVariableBlock()
    getVariableBlock2.setRawInput("n")

    val endFunctionBlock1 = EndFunctionBlock()
    endFunctionBlock1.setPreviousMainFlowBlock(functionBlock)
    endFunctionBlock1.setOperator(getVariableBlock2)
    functionBlock.setNextMainFlowBlock(endFunctionBlock1)

    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
*/

/*
    val startBlock = StartBlock()
    val endBlock = EndBlock()

    val initializationVariableBlock1 = InitializationAndSetVariableBlock()
    initializationVariableBlock1.setRawInput("i=10")
    startBlock.setNextMainFlowBlock(initializationVariableBlock1)
    initializationVariableBlock1.setPreviousMainFlowBlock(startBlock)

    val callFunctionBlock1 = CallFunctionBlock()
    callFunctionBlock1.setRawInput("print(i)")
    initializationVariableBlock1.setNextMainFlowBlock(callFunctionBlock1)
    callFunctionBlock1.setPreviousMainFlowBlock(initializationVariableBlock1)

    callFunctionBlock1.setNextMainFlowBlock(endBlock)
    endBlock.setPreviousMainFlowBlock(callFunctionBlock1)

//function
    val functionBlock = FunctionBlock()
    functionBlock.setRawInput("print(n)")

    val changeBlock = InitializationAndSetVariableBlock()
    changeBlock.setRawInput("n=15")
    functionBlock.setNextMainFlowBlock(changeBlock)

    val printBlock = PrintBlock()
    printBlock.setPreviousMainFlowBlock(changeBlock)
    changeBlock.setNextMainFlowBlock(printBlock)

    val getVariableBlock = GetVariableBlock()
    getVariableBlock.setRawInput("n")
    printBlock.setOperator(getVariableBlock)

    val endFunctionBlock = EndFunctionBlock()
    endFunctionBlock.setPreviousMainFlowBlock(printBlock)
    printBlock.setNextMainFlowBlock(endFunctionBlock)

    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
*/

/*
val startBlock = StartBlock()
    val endBlock = EndBlock()

    val initializationVariableBlock1 = InitializationAndSetVariableBlock()
    startBlock.setNextMainFlowBlock(initializationVariableBlock1)
    initializationVariableBlock1.setRawInput("n=15,*arr[n]")

    val forBlock1 = ForBlock()
    initializationVariableBlock1.setNextMainFlowBlock(forBlock1)
    forBlock1.setRawInput("i=0,i<n,i+=1")
    forBlock1.setPreviousMainFlowBlock(initializationVariableBlock1)

    val variableChangeBlock1 = InitializationAndSetVariableBlock()
    forBlock1.setTrueExpressionBranch(variableChangeBlock1)
    variableChangeBlock1.setRawInput("arr[i]=(100*rand()-50).toInt()")
    variableChangeBlock1.setPreviousMainFlowBlock(forBlock1)

    val forBlock2 = ForBlock()
    forBlock2.setRawInput("i=0,i<n,i+=1")

    val printBlock2 = PrintBlock()
    forBlock1.setFalseExpressionBranch(printBlock2)
    val getVariableBlock3 = GetVariableBlock()
    getVariableBlock3.setRawInput("arr")
    printBlock2.setOperator(getVariableBlock3)
    printBlock2.setPreviousMainFlowBlock(forBlock1)
    printBlock2.setNextMainFlowBlock(forBlock2)
    forBlock2.setPreviousMainFlowBlock(printBlock2)

    val forBlock3 = ForBlock()
    forBlock3.setPreviousMainFlowBlock(forBlock2)
    forBlock2.setTrueExpressionBranch(forBlock3)
    forBlock3.setRawInput("j=i+1,j<n,j+=1")

    val ifBlock1 = IfBlock()
    ifBlock1.setPreviousMainFlowBlock(forBlock3)
    forBlock3.setTrueExpressionBranch(ifBlock1)

    val getVariableBlock1 = GetVariableBlock()
    getVariableBlock1.setRawInput("arr[i]>arr[j]")
    ifBlock1.setOperator(getVariableBlock1)

    val initializationVariableBlock2 = InitializationAndSetVariableBlock()
    initializationVariableBlock2.setRawInput("t=arr[i]")
    ifBlock1.setTrueExpressionBranch(initializationVariableBlock2)
    initializationVariableBlock2.setPreviousMainFlowBlock(ifBlock1)

    val variableChangeBlock2 = InitializationAndSetVariableBlock()
    variableChangeBlock2.setRawInput("arr[i]=arr[j],arr[j]=t")
    initializationVariableBlock2.setNextMainFlowBlock(variableChangeBlock2)
    variableChangeBlock2.setPreviousMainFlowBlock(initializationVariableBlock2)

    val endIfBlock1 = EndIfBlock()
    variableChangeBlock2.setNextMainFlowBlock(endIfBlock1)
    ifBlock1.setFalseExpressionBranch(endIfBlock1)
    endIfBlock1.setTrueExpressionBranch(variableChangeBlock2)
    endIfBlock1.setFalseExpressionBranch(ifBlock1)

    val printBlock1 = PrintBlock()
    forBlock2.setFalseExpressionBranch(printBlock1)
    printBlock1.setPreviousMainFlowBlock(forBlock2)
    printBlock1.setNextMainFlowBlock(endBlock)

    val getVariableBlock2 = GetVariableBlock()
    getVariableBlock2.setRawInput("arr")
    printBlock1.setOperator(getVariableBlock2)
    endBlock.setPreviousMainFlowBlock(printBlock1)

    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
*/

/*
    val startBlock = StartBlock()
    val endBlock = EndBlock()

    val getVariableBlock1 = GetVariableBlock()
    getVariableBlock1.setRawInput("1")
    val getVariableBlock2 = GetVariableBlock()
    getVariableBlock2.setRawInput("2")
    val getVariableBlock3 = GetVariableBlock()
    getVariableBlock3.setRawInput("4")

    val binarySumOperatorBlock1 = BinarySumOperatorBlock()
    binarySumOperatorBlock1.setLeftOperator(getVariableBlock1)
    binarySumOperatorBlock1.setRightOperator(getVariableBlock2)

    val binarySubOperatorBlock1 = BinarySubOperatorBlock()
    binarySubOperatorBlock1.setLeftOperator(binarySumOperatorBlock1)
    binarySubOperatorBlock1.setRightOperator(getVariableBlock3)

    val binaryMulOperatorBlock1 = BinaryMulOperatorBlock()
    binaryMulOperatorBlock1.setLeftOperator(binarySumOperatorBlock1)
    binaryMulOperatorBlock1.setRightOperator(binarySubOperatorBlock1)

    val ifBlock1 = IfBlock()
    ifBlock1.setOperator(binaryMulOperatorBlock1)
    ifBlock1.setPreviousMainFlowBlock(startBlock)

    val printBlock1 = PrintBlock()
    printBlock1.setOperator(binaryMulOperatorBlock1)
    printBlock1.setPreviousMainFlowBlock(ifBlock1)

    val endIfBlock1 = EndIfBlock()
    endIfBlock1.setTrueExpressionBranch(printBlock1)
    endIfBlock1.setFalseExpressionBranch(ifBlock1)
    endIfBlock1.setNextMainFlowBlock(endBlock)

    printBlock1.setNextMainFlowBlock(endIfBlock1)
    startBlock.setNextMainFlowBlock(ifBlock1)
    endBlock.setPreviousMainFlowBlock(endIfBlock1)

    ifBlock1.setTrueExpressionBranch(printBlock1)
    ifBlock1.setFalseExpressionBranch(endIfBlock1)

    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
*/

/*
    val startBlock = StartBlock()
    val endBlock = EndBlock()

    val forBlock = ForBlock()
    val printBlock = PrintBlock()
    val getVariableBlock = GetVariableBlock()

    getVariableBlock.setRawInput("n")

    startBlock.setNextMainFlowBlock(forBlock)
    forBlock.setPreviousMainFlowBlock(startBlock)
    forBlock.setRawInput("n=0,n<15,n+=1")
    forBlock.setTrueExpressionBranch(printBlock)
    forBlock.setFalseExpressionBranch(endBlock)
    printBlock.setOperator(getVariableBlock)
    printBlock.setPreviousMainFlowBlock(forBlock)
    endBlock.setPreviousMainFlowBlock(forBlock)

    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
*/
