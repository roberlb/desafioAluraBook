package com.alura.literatura.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> clase);
}
