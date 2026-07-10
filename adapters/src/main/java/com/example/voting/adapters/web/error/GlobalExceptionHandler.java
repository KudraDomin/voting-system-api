package com.example.voting.adapters.web.error;

import java.net.URI;
import java.util.stream.Collectors;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.voting.core.exception.BlockedVoterCannotVoteException;
import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.exception.OptionNotInElectionException;
import com.example.voting.core.exception.VoterAlreadyBlockedException;
import com.example.voting.core.exception.VoterNotBlockedException;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.exception.ElectionOptionNotFoundException;
import com.example.voting.usecases.exception.VoterAlreadyExistsException;
import com.example.voting.usecases.exception.VoterNotFoundException;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_CODE_PROPERTY = "errorCode";

    private static final String CORRELATION_ID_PROPERTY = "correlationId";

    private static final String PROBLEM_TYPE_BASE = "https://voting-system.example.com/problems/";

    private static final String VALIDATION_FAILED_DETAIL = "Request validation failed";

    private static final String INTERNAL_ERROR_DETAIL = "An unexpected error occurred";

    private static final String FIELD_ERROR_SEPARATOR = "; ";

    private static final String FIELD_ERROR_FORMAT = "%s: %s";

    private static final String TYPE_MISMATCH_FORMAT = "%s: invalid value '%s'";

    private static final String MALFORMED_BODY_DETAIL = "Malformed request body";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        logClientError(ErrorCode.VALIDATION_FAILED, exception);

        return problemDetail(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, describeFieldErrors(exception));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        logClientError(ErrorCode.VALIDATION_FAILED, exception);

        return problemDetail(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        logClientError(ErrorCode.VALIDATION_FAILED, exception);

        var detail = TYPE_MISMATCH_FORMAT.formatted(exception.getName(), exception.getValue());

        return problemDetail(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, detail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableMessage(HttpMessageNotReadableException exception) {
        logClientError(ErrorCode.VALIDATION_FAILED, exception);

        return problemDetail(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, MALFORMED_BODY_DETAIL);
    }

    @ExceptionHandler({
            VoterNotFoundException.class,
            ElectionNotFoundException.class,
            ElectionOptionNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException exception) {
        logClientError(ErrorCode.RESOURCE_NOT_FOUND, exception);

        return problemDetail(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(VoterAlreadyExistsException.class)
    public ProblemDetail handleVoterAlreadyExists(VoterAlreadyExistsException exception) {
        logClientError(ErrorCode.VOTER_ALREADY_EXISTS, exception);

        return problemDetail(HttpStatus.CONFLICT, ErrorCode.VOTER_ALREADY_EXISTS, exception.getMessage());
    }

    @ExceptionHandler(VoterAlreadyBlockedException.class)
    public ProblemDetail handleVoterAlreadyBlocked(VoterAlreadyBlockedException exception) {
        logClientError(ErrorCode.VOTER_ALREADY_BLOCKED, exception);

        return problemDetail(HttpStatus.CONFLICT, ErrorCode.VOTER_ALREADY_BLOCKED, exception.getMessage());
    }

    @ExceptionHandler(VoterNotBlockedException.class)
    public ProblemDetail handleVoterNotBlocked(VoterNotBlockedException exception) {
        logClientError(ErrorCode.VOTER_NOT_BLOCKED, exception);

        return problemDetail(HttpStatus.CONFLICT, ErrorCode.VOTER_NOT_BLOCKED, exception.getMessage());
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public ProblemDetail handleDuplicateVote(DuplicateVoteException exception) {
        logClientError(ErrorCode.DUPLICATE_VOTE, exception);

        return problemDetail(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_VOTE, exception.getMessage());
    }

    @ExceptionHandler(BlockedVoterCannotVoteException.class)
    public ProblemDetail handleBlockedVoterCannotVote(BlockedVoterCannotVoteException exception) {
        logClientError(ErrorCode.VOTER_BLOCKED, exception);

        return problemDetail(HttpStatus.CONFLICT, ErrorCode.VOTER_BLOCKED, exception.getMessage());
    }

    @ExceptionHandler(OptionNotInElectionException.class)
    public ProblemDetail handleOptionNotInElection(OptionNotInElectionException exception) {
        logClientError(ErrorCode.OPTION_NOT_IN_ELECTION, exception);

        return problemDetail(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.OPTION_NOT_IN_ELECTION, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception, WebRequest request) {
        log.error("Unhandled exception for request {}", request.getDescription(false), exception);

        return problemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, INTERNAL_ERROR_DETAIL);
    }

    private ProblemDetail problemDetail(HttpStatus status, ErrorCode errorCode, String detail) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(errorCode.name());
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE + errorCode.name().toLowerCase()));
        problemDetail.setProperty(ERROR_CODE_PROPERTY, errorCode.name());
        problemDetail.setProperty(CORRELATION_ID_PROPERTY, currentCorrelationId());

        return problemDetail;
    }

    private String describeFieldErrors(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> FIELD_ERROR_FORMAT.formatted(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(FIELD_ERROR_SEPARATOR));

        if (fieldErrors.isEmpty()) {
            return VALIDATION_FAILED_DETAIL;
        }

        return fieldErrors;
    }

    private String currentCorrelationId() {
        return MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
    }

    private void logClientError(ErrorCode errorCode, Exception exception) {
        log.warn("Request rejected with {}: {}", errorCode, exception.getMessage());
    }
}
