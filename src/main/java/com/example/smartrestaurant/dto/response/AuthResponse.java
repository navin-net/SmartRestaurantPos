package com.example.smartrestaurant.dto.response;

import java.util.List;
public record AuthResponse(String accessToken, long expiresInMs, String username, List<String> authorities) {}