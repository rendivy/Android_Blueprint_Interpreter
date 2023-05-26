package exceptions

class RuntimeError(message: String = "") : Exception(message)

class TypeError(message: String = "") :
    Exception("FATAL ERROR:\nTypeError: $message\n\nStack traceback:")

class OperationError(message: String = "") :
    Exception("FATAL ERROR:\nOperationError: $message\n\nStack traceback:")

class StackCorruptionError(message: String = "") :
    Exception("FATAL ERROR:\nStackCorruptionError: $message\n\nStack traceback:")

class IndexOutOfRangeError(message: String = "") :
    Exception("FATAL ERROR:\nIndexOutOfRangeError: $message\n\nStack traceback:")

class NullPointerExceptionInOperator(message: String = "") :
    Exception("FATAL ERROR:\nNullPointerExceptionInOperator: $message\n\nStack traceback:")

class NotFoundError(message: String = "") :
    Exception("FATAL ERROR:\nNotFoundError: $message\n\nStack traceback:")