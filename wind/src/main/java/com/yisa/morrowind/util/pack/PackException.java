package com.yisa.morrowind.util.pack;

/**
 * Created by Yisa on 2017/7/27.
 */
public class PackException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public PackException() {
        this("PackError");
    }

    public PackException(String message) {
        super(message);
    }

    public PackException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackException(Throwable cause) {
        super(cause);
    }
}
