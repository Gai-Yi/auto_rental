package com.coder.rental.util;

import com.coder.rental.entity.Dept;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeptTreeUtil {
    public static List<Dept> buildDeptTree(List<Dept> deptList, int pid) {
        List<Dept> deptTree = new ArrayList<>();
        Optional.ofNullable(deptList).orElse(new ArrayList<>())
                .stream().filter(dept -> dept != null && dept.getPid() == pid)
                .forEach(
                        dept -> {
                            // 这里拷贝一份是为了不修改原来的deptList数据
                            Dept dept1 = new Dept();
                            BeanUtils.copyProperties(dept, dept1);
                            dept1.setChildren(buildDeptTree(deptList, dept1.getId()));
                            deptTree.add(dept1);
                        }
                );
        return deptTree;
    }
}
