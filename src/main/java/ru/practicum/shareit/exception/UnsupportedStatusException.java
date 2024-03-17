package ru.practicum.shareit.exception;

public class UnsupportedStatusException extends FormatMassage {

    public UnsupportedStatusException(String massage) {
        super(massage);
    }

    public UnsupportedStatusException(String massage, Object param) {
        super(massage, param);
    }

    public UnsupportedStatusException(String massage, Object param1, Object param2) {
        super(massage, param1, param2);
    }

    public UnsupportedStatusException(String massage, Object param1, Object param2, Object param3) {
        super(massage, param1, param2, param3);
    }
}
