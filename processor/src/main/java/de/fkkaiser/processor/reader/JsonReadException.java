package de.fkkaiser.processor.reader;

/**
 * Should be thrown if a json-file is not readable.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class JsonReadException extends Exception{

    public JsonReadException(String message){
        super(message);
    }


    public JsonReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
