package ru.practicum.shareit.exception;

public class ValidException extends FormatMassage {
    public ValidException(String massage) {
        super(massage);
    }

    public ValidException(String massage, Object param) {
        super(massage, param);
    }

    public ValidException(String massage, Object param1, Object param2) {
        super(massage, param1, param2);
    }

    public ValidException(String massage, Object param1, Object param2, Object param3) {
        super(massage, param1, param2, param3);
    }
}
