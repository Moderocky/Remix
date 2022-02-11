package mx.kenzie.remix.parser;

public enum ConsumerFlag implements Flag {
    HEADER_TYPE_NAME,
    HEADER_TYPE_EXT,
    HEADER_FUNC_NAME,
    HEADER_FUNC_PARAM_TYPE,
    HEADER_FUNC_PARAM_NAME,
    CAST,
    ALLOCATE,
    NEW,
    HEADER_VAR_NAME,
}
