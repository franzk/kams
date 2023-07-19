package net.franzka.kams.gateway.exception;

public class ExpiredTokenException extends Exception {

    public ExpiredTokenException() {
        super("Token Expired");
    }

}
