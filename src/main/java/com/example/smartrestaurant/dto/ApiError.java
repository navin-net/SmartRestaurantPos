package com.example.smartrestaurant.dto;

public record ApiError(int status, String error, String message) {}