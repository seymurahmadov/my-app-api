package com.company.myappapi.handler;


import com.company.myappapi.dto.validation.ErrorDetail;
import com.company.myappapi.dto.validation.ValidationError;
import com.company.myappapi.exception.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;
    private static final Map<String, String> constraint = Map.of("PriorityDeadlineConstraint", "deadline", "RequestItemConstraint", "requestType");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorDetail = ErrorDetail.builder()
                .title(messageSource.getMessage("default.title", ArrayUtils.EMPTY_STRING_ARRAY, Locale.getDefault()))
                .timeStamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(messageSource.getMessage("default.detail", ArrayUtils.EMPTY_STRING_ARRAY, Locale.getDefault()))
                .errors(new HashMap<>())
                .build();

        fieldError(manve, errorDetail);
        objError(manve, errorDetail);

        return handleExceptionInternal(manve, errorDetail, headers, status, request);
    }

    private void fieldError(MethodArgumentNotValidException manve, ErrorDetail errorDetail) {
        for (FieldError fe : manve.getBindingResult().getFieldErrors()) {
            var validationErrorList = errorDetail.getErrors().computeIfAbsent(fe.getField(), k -> new ArrayList<>());
            var validationError = ValidationError.of(fe.getCode(), messageSource.getMessage(fe, Locale.getDefault()));
            validationErrorList.add(validationError);
        }
    }

    private void objError(MethodArgumentNotValidException manve, ErrorDetail errorDetail) {
        for (ObjectError oe : manve.getAllErrors()) {
            var key = constraint.entrySet().stream().filter(m -> Arrays.asList(oe.getCodes()).contains(m.getKey())).findFirst();

            key.ifPresent(x -> {
                var validationErrorList = errorDetail.getErrors().computeIfAbsent(x.getValue(), k -> new ArrayList<>());
                var validationError = ValidationError.of(oe.getCode(), messageSource.getMessage(oe, Locale.getDefault()));
                validationErrorList.add(validationError);
            });
        }
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorDetail = ErrorDetail.builder()
                .title("Message Not Readable")
                .timeStamp(new Date().getTime())
                .status(status.value())
                .detail(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ExceptionHandler(PercentageOverflowException.class)
    public ResponseEntity<Object> handlePercentageOverflow(PercentageOverflowException ex) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TargetCountOverflowException.class)
    public ResponseEntity<Object> handlePercentageOverflow(TargetCountOverflowException ex) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({GenerateException.class})
    public ResponseEntity<Object> gen(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> userNotFound(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenEvaluationOperationException.class)
    public ResponseEntity<Object> forbiddenEvaluation(ForbiddenEvaluationOperationException ex) {
        log.error(ex.getMessage(), ex);
        return response("forbidden.evaluation",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeightSumMustBeHundredException.class)
    public ResponseEntity<Object> weightMustBeHundred(WeightSumMustBeHundredException ex) {
        log.error(ex.getMessage(), ex);
        return response("weight.must.be.hundred",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeightCannotBeZeroException.class)
    public ResponseEntity<Object> weightCannotBeZero(WeightCannotBeZeroException ex) {
        log.error(ex.getMessage(), ex);
        return response("weight.cannot.be.zero",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TargetCountException.class)
    public ResponseEntity<Object> targetCount(TargetCountException ex) {
        log.error(ex.getMessage(), ex);
        return response("target.cannot.between.three.five",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenTargetCreateException.class)
    public ResponseEntity<Object> forbiddenTargetCreate(ForbiddenTargetCreateException ex) {
        log.error(ex.getMessage(), ex);
        return response("forbidden.target.create",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenTargetUpdateException.class)
    public ResponseEntity<Object> forbiddenTargetUpdate(ForbiddenTargetUpdateException ex) {
        log.error(ex.getMessage(), ex);
        return response("forbidden.target.update",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnEvaluateTargetException.class)
    public ResponseEntity<Object> unEvaluatedTargets(UnEvaluateTargetException ex) {
        log.error(ex.getMessage(), ex);
        return response("unevaluated.targets",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenDeleteException.class)
    public ResponseEntity<Object> forbiddenDeleteTarget(ForbiddenDeleteException ex) {
        log.error(ex.getMessage(), ex);
        return response("forbidden.target.delete",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> notFound(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response("data.not.found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({OptimisticLockingFailureException.class})
    public ResponseEntity<Object> lock(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response("opt.lock.msg", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> access(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response("access.denied.msg", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> ex(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response("error.msg", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<Object> conflict(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<Object> forbiddenEx(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return response(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Object> response(String msg, HttpStatus status) {
        var errorDetail = ErrorDetail.builder()
                .title(messageSource.getMessage(msg, ArrayUtils.EMPTY_STRING_ARRAY, Locale.getDefault()))
                .timeStamp(new Date().getTime())
                .status(status.value())
                .detail(messageSource.getMessage(msg, ArrayUtils.EMPTY_STRING_ARRAY, Locale.getDefault()))
                .build();

        return new ResponseEntity<>(errorDetail, status);
    }
}