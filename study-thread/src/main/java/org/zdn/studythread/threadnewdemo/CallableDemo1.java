package org.zdn.studythread.threadnewdemo;

import org.zdn.studythread.vo.Student;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CallableDemo1 implements Callable<Student> {
    @Override
    public Student call() throws Exception {
        Student student = new Student();
        student.setName("zdn1");
        student.setAge(18);
        student.setSex("男");
        student.setGradeList(new HashMap<>(Map.of(
                "语文", new BigDecimal(99),
                "数学", new BigDecimal(99),
                "英语", new BigDecimal(99),
                "物理", new BigDecimal(99),
                "化学", new BigDecimal(99),
                "生物", new BigDecimal(99)
        )));
        return student;
    }
}
