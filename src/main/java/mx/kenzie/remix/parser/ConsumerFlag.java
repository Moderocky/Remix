package mx.kenzie.remix.parser;

public enum ConsumerFlag implements Flag {
    HEADER_TYPE_NAME,
    HEADER_TYPE_EXT,
    HEADER_FUNC_NAME,
    HEADER_FUNC_PARAM_TYPE,
    HEADER_FUNC_PARAM_NAME,
    HEADER_OPER_NAME,
    HEADER_OPER_PARAM_TYPE,
    HEADER_OPER_PARAM_NAME,
    CAST,
    ALLOCATE,
    NEW,
    INSTANCE,
    HEADER_VAR_NAME,
}
