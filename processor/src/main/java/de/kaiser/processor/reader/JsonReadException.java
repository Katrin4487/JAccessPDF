package de.kaiser.processor.reader;

/**
 * Should be thrown if a json-file is not readable.
 */
public class JsonReadException extends Exception{

    public JsonReadException(String message){
        super(message);
    }
}
