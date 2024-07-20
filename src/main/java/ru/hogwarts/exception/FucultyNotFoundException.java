package ru.hogwarts.exception;

public class FucultyNotFoundException extends  NotFoundException{
    public FucultyNotFoundException(long id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "Факультет с id = %d не найден!".formatted(getId());
    }
}
