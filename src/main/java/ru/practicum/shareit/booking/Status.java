package ru.practicum.shareit.booking;

public enum Status {
    WAITING, // — новое бронирование, ожидает одобрения
    APPROVED, //— Дополнительные советы ментора 2 бронирование подтверждено владельцем
    REJECTED, //— бронирование отклонено владельцем
    CANCELED //— бронирование отменено создателем
}
