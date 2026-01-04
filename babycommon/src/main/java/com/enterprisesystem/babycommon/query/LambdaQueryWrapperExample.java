package com.enterprisesystem.babycommon.query;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * LambdaQueryWrapper 使用示例
 *
 * @author Claude Code
 * @date 2026-01-04
 */
public class LambdaQueryWrapperExample {

    /**
     * 示例实体类
     */
    public static class User {
        private Long id;
        private String userName;
        private String email;
        private Integer age;
        private Integer status;
        private String createTime;

        public Long getId() {
            return id;
        }

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public Integer getAge() {
            return age;
        }

        public Integer getStatus() {
            return status;
        }

        public String getCreateTime() {
            return createTime;
        }
    }

    /**
     * 示例 1：基本查询 - 等于条件
     */
    public static void example1() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1);

        System.out.println("示例 1 - SQL: " + wrapper.getSql());
        System.out.println("示例 1 - 参数: " + wrapper.getParams());
        // 输出: WHERE status = ?
        // 参数: [1]
    }

    /**
     * 示例 2：模糊查询
     */
    public static void example2() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getUserName, "张")
               .like(User::getEmail, "gmail");

        System.out.println("示例 2 - SQL: " + wrapper.getSql());
        System.out.println("示例 2 - 参数: " + wrapper.getParams());
        // 输出: WHERE user_name LIKE ? AND email LIKE ?
        // 参数: [%张%, %gmail%]
    }

    /**
     * 示例 3：范围查询
     */
    public static void example3() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(User::getAge, 18, 30);

        System.out.println("示例 3 - SQL: " + wrapper.getSql());
        System.out.println("示例 3 - 参数: " + wrapper.getParams());
        // 输出: WHERE age BETWEEN ? AND ?
        // 参数: [18, 30]
    }

    /**
     * 示例 4：IN 查询
     */
    public static void example4() {
        List<Integer> statusList = Arrays.asList(1, 2, 3);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getStatus, statusList);

        System.out.println("示例 4 - SQL: " + wrapper.getSql());
        System.out.println("示例 4 - 参数: " + wrapper.getParams());
        // 输出: WHERE status IN (?,?,?)
        // 参数: [1, 2, 3]
    }

    /**
     * 示例 5：排序查询
     */
    public static void example5() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1)
               .orderByDesc(User::getCreateTime)
               .orderByAsc(User::getId);

        System.out.println("示例 5 - SQL: " + wrapper.getSql());
        System.out.println("示例 5 - 参数: " + wrapper.getParams());
        // 输出: WHERE status = ? ORDER BY create_time DESC, id ASC
        // 参数: [1]
    }

    /**
     * 示例 6：组合查询
     */
    public static void example6() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1)
               .like(User::getUserName, "李")
               .gt(User::getAge, 18)
               .lt(User::getAge, 60)
               .orderByDesc(User::getCreateTime);

        System.out.println("示例 6 - SQL: " + wrapper.getSql());
        System.out.println("示例 6 - 参数: " + wrapper.getParams());
        // 输出: WHERE status = ? AND user_name LIKE ? AND age > ? AND age < ? ORDER BY create_time DESC
        // 参数: [1, %李%, 18, 60]
    }

    /**
     * 示例 7：动态条件查询（实际开发场景）
     */
    public static void example7(String userName, Integer minAge, Integer maxAge, String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 根据参数是否为空动态添加条件
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like(User::getUserName, userName);
        }

        if (minAge != null) {
            wrapper.ge(User::getAge, minAge);
        }

        if (maxAge != null) {
            wrapper.le(User::getAge, maxAge);
        }

        if (StringUtils.isNotBlank(email)) {
            wrapper.eq(User::getEmail, email);
        }

        wrapper.eq(User::getStatus, 1)
               .orderByDesc(User::getCreateTime);

        System.out.println("示例 7 - SQL: " + wrapper.getSql());
        System.out.println("示例 7 - 参数: " + wrapper.getParams());
    }

    /**
     * 示例 8：自定义 SQL 片段
     */
    public static void example8() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1)
               .apply("YEAR(create_time) = ?", 2024)
               .apply("MONTH(create_time) IN (1, 2, 3)");

        System.out.println("示例 8 - SQL: " + wrapper.getSql());
        System.out.println("示例 8 - 参数: " + wrapper.getParams());
        // 输出: WHERE status = ? AND YEAR(create_time) = ? AND MONTH(create_time) IN (1, 2, 3)
        // 参数: [1, 2024]
    }

    /**
     * 示例 9：空值判断
     */
    public static void example9() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(User::getEmail)
               .eq(User::getStatus, 1);

        System.out.println("示例 9 - SQL: " + wrapper.getSql());
        System.out.println("示例 9 - 参数: " + wrapper.getParams());
        // 输出: WHERE email IS NOT NULL AND status = ?
        // 参数: [1]
    }

    /**
     * 示例 10：复杂查询
     */
    public static void example10() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1)
               .in(User::getId, Arrays.asList(1L, 2L, 3L))
               .between(User::getAge, 18, 30)
               .likeRight(User::getEmail, "test")
               .orderByDesc(User::getCreateTime)
               .orderByAsc(User::getUserName);

        System.out.println("示例 10 - SQL: " + wrapper.getSql());
        System.out.println("示例 10 - 参数: " + wrapper.getParams());
        // 输出: WHERE status = ? AND id IN (?,?,?) AND age BETWEEN ? AND ? AND email LIKE ? ORDER BY create_time DESC, user_name ASC
        // 参数: [1, 1, 2, 3, 18, 30, test%]
    }

    /**
     * 运行所有示例
     */
    public static void main(String[] args) {
        System.out.println("=== LambdaQueryWrapper 使用示例 ===\n");

        example1();
        System.out.println();

        example2();
        System.out.println();

        example3();
        System.out.println();

        example4();
        System.out.println();

        example5();
        System.out.println();

        example6();
        System.out.println();

        example7("张三", 20, 30, "test@example.com");
        System.out.println();

        example8();
        System.out.println();

        example9();
        System.out.println();

        example10();
    }
}
