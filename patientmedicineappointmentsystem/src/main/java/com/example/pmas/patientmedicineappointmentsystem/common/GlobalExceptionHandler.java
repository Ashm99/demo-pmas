package com.example.pmas.patientmedicineappointmentsystem.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * A method to handle exceptions of type MethodArgumentNotValidException
     * @param exception
     * @return A response entity with the validation error messages as its body.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String field = ((FieldError)error).getField();
                    String value = error.getDefaultMessage();
                    errors.put(field, value);
                }
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * A method to handle exceptions of type MethodArgumentTypeMismatchException
     * @param exception
     * @return A response entity with the validation error messages as its body.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException exception){
        String error = String.format("The field '%s' should be of type '%s'.", exception.getName(), exception.getRequiredType().getSimpleName());
//        System.out.println(
//                "exception.getMessage(): " + exception.getMessage() + "\n" +
//                "exception.getName(): " + exception.getName() + "\n" +
//                "exception.getParameter(): " + exception.getParameter() + "\n" +
//                "exception.getValue(): " + exception.getValue() + "\n" +
//                "exception.getErrorCode(): " + exception.getErrorCode() + "\n" +
//                "exception.getPropertyName(): " + exception.getPropertyName() + "\n" +
//                "exception.getRequiredType(): " + exception.getRequiredType() + "\n" +
//                "exception.getRequiredType().getSimpleName(): " + exception.getRequiredType().getSimpleName() + "\n" +
//                "exception.getRequiredType().getName(): " + exception.getRequiredType().getName() + "\n" +
//                "exception.getRequiredType().getFields(): " + exception.getRequiredType().getFields()
//        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /**
     * A method to handle exceptions of type NoSuchElementException
     * @param exception
     * @return A response entity with the exception message as its body.
     * @throws InterruptedException
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleDataNotFoundException(NoSuchElementException exception) throws InterruptedException{
        System.err.println("Exception occurred. There is no data as per your request. Check stack trace for further info.");
        System.err.print("Stack trace:");
        Thread.sleep(1000);
        exception.printStackTrace();
        Thread.sleep(1000);
        System.out.println("Stack trace printed | Resuming application.");
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * A method to handle general exception
     * @param exception
     * @return A response entity with the exception message as its body.
     * @throws InterruptedException
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) throws InterruptedException{
        System.err.println("Exception occurred. Check stack trace for further info.");
        System.err.print("Stack trace:");
        Thread.sleep(1000);
        exception.printStackTrace();
        Thread.sleep(1000);
        System.out.println("Stack trace printed | Resuming application.");
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
