package com.powerstrike.dto;

import lombok.Data;

@Data
public class AttendanceRequest {
    private Long userId;
    private String notes;
}
