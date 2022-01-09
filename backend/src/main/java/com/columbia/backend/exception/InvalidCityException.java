package com.columbia.backend.exception;

public class InvalidCityException extends Exception{
    public InvalidCityException(){
        super();
    }

    public InvalidCityException(String msg){
        super(msg);
    }
}
