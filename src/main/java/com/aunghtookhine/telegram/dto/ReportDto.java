package com.aunghtookhine.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ReportDto {
    private String title;
    private String description;
    private LocalDate date;
    private String createdBy;
}
