package ru.mts.account.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mts.account.exception.CustomException;
import ru.mts.account.model.ExceptionData;
import ru.mts.account.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обрабатывает исключения типа CustomException путем преобразования исключения
     * в соответствующий ответ с HTTP статусом BAD_REQUEST
     *
     * @param e исключение, которое необходимо обработать
     * @return ResponseEntity с информацией об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<ExceptionData>> handleException(CustomException e) {
        ExceptionResponse<ExceptionData> exceptionResponse =
                new ExceptionResponse<>(new ExceptionData(e.getCode(), e.getMessage()));

        log.error("Произошла ошибка: {}, Код ошибки: {}, Сообщение ошибки: {}", e.getClass().getSimpleName(), e.getCode(), e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}

