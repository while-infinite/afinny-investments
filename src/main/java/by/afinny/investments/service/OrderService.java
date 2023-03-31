package by.afinny.investments.service;

import by.afinny.investments.dto.RequestNewAccountAgreeDto;
import by.afinny.investments.dto.ResponseNewAccountAgreeDto;

public interface OrderService {

    ResponseNewAccountAgreeDto registerNewAccountAgree(RequestNewAccountAgreeDto requestNewAccountAgreeDto);
}
