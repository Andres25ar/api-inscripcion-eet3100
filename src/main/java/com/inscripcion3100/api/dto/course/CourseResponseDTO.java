package com.inscripcion3100.api.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Integer year;
    private Integer studyYear;
    private Integer division;
    private Boolean advancedCycle;
    private Integer maxCapacity;
    private String shift;
    private String speciality;
}
