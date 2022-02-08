package com.epam.spring.library.controller.handler;

import com.epam.spring.library.dto.ErrorDTO;
import com.epam.spring.library.exception.ConflictException;
import com.epam.spring.library.exception.EntityNotFoundException;
import com.epam.spring.library.exception.ErrorType;
import com.epam.spring.library.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.spring.library.exception.ErrorType.FATAL_ERROR;
import static com.epam.spring.library.exception.ErrorType.VALIDATION_ERROR;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HandlerMethod hm) {
        log(ex, hm);
        return ex.getAllErrors()
                 .stream()
                 .map(e -> getErrorDTO(e.getDefaultMessage(), VALIDATION_ERROR))
                 .collect(Collectors.toList());
    }

    private ErrorDTO getErrorDTO(String msg, ErrorType errorType) {
        return ErrorDTO.builder()
                       .message(msg)
                       .errorType(errorType)
                       .timestamp(Instant.now())
                       .build();
    }

    private ErrorDTO getErrorDTO(ServiceException ex) {
        return getErrorDTO(ex.getMessage(), ex.getErrorType());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleEntityNotFoundException(EntityNotFoundException ex,
                                                  HandlerMethod hm) {
        return handleCustomException(ex, hm);
    }

    private ErrorDTO handleCustomException(ServiceException ex,
                                           HandlerMethod hm) {
        log(ex, hm);
        return getErrorDTO(ex);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleConflictException(ConflictException ex,
                                            HandlerMethod hm) {
        return handleCustomException(ex, hm);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleServiceException(ServiceException ex,
                                           HandlerMethod hm) {
        return handleCustomException(ex, hm);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception ex, HandlerMethod hm) {
        log(ex, hm);
        return getErrorDTO(ex.getMessage(), FATAL_ERROR);
    }

    private void log(Exception ex, HandlerMethod hm) {
        log.error("{}.{} throws {} with message: {}",
                  hm.getMethod().getDeclaringClass().getSimpleName(),
                  hm.getMethod().getName(), ex.getClass().getSimpleName(),
                  ex.getMessage());
    }
}