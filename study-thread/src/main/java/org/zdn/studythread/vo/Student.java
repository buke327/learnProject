package org.zdn.studythread.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class Student implements Serializable {

    private String name;

    private int age;

    private String sex;

    private Map<String, BigDecimal> gradeList;
}
