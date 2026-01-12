package org.zdn.studythread.threadnewdemo;

import org.jspecify.annotations.NonNull;
import org.zdn.studythread.vo.Student;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CallableDemo2 implements Callable<Student> {
    @Override
    public Student call() throws Exception {
        Student student = new Student();
        student.setName("zdn");
        student.setAge(18);
        student.setSex("男");
        student.setGradeList(new HashMap<>(Map.of(
                "语文", new BigDecimal(100),
                "数学", new BigDecimal(100),
                "英语", new BigDecimal(100),
                "物理", new BigDecimal(100),
                "化学", new BigDecimal(100),
                "生物", new BigDecimal(100)
        )));
        return student;
    }
}
