package vn.hoidanit.jobhunter.service.error;

public class IdInvalidException extends Exception {
    // Parameterless Constructor
    public IdInvalidException() {}

    // Constructor that accepts a message
    public IdInvalidException(String message)
    {
       super(message);
    }
}
