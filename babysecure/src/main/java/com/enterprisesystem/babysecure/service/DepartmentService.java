package com.enterprisesystem.babysecure.service;

import com.enterprisesystem.babysecure.model.dto.DepartmentDto;

import java.util.List;

/**
 * 部门 Service 接口
 *
 * 【为什么要先定义接口？】
 * 1. 遵循"面向接口编程"的设计原则
 * 2. Controller 只依赖接口，不依赖具体实现
 * 3. 便于扩展和维护（比如以后有多个实现类）
 * 4. 便于单元测试（可以 Mock 接口）
 *
 * 【Service 层的职责】
 * - 定义业务方法（不是 SQL 操作，是业务操作）
 * - 方法名体现业务含义（如 addDepartment 而不是 insertDepartment）
 */
public interface DepartmentService {

    // ==================== 增删改操作 ====================

    /**
     * 添加部门
     *
     * 业务逻辑：
     * 1. 校验部门编码是否已存在
     * 2. 如果有父部门，校验父部门是否存在
     * 3. 保存到数据库
     *
     * @param departmentDto 部门信息
     * @return 保存后的部门对象（包含自动生成的 ID）
     */
    DepartmentDto addDepartment(DepartmentDto departmentDto);

    /**
     * 更新部门信息
     *
     * 业务逻辑：
     * 1. 校验部门是否存在
     * 2. 如果修改了编码，校验新编码是否重复
     * 3. 如果修改了父部门，不能设置为自己或自己的子部门
     * 4. 更新数据库
     *
     * @param departmentDto 部门信息（必须包含 ID）
     * @return 更新后的部门对象
     */
    DepartmentDto updateDepartment(DepartmentDto departmentDto);

    /**
     * 删除部门
     *
     * 业务逻辑：
     * 1. 校验部门是否存在
     * 2. 检查是否有子部门，如果有则不允许删除
     * 3. 检查是否有员工，如果有则不允许删除
     * 4. 删除数据库记录
     *
     * ⚠️ 注意：这是物理删除，也可以实现逻辑删除（将 status 设置为 0）
     *
     * @param id 部门ID
     * @return 是否删除成功
     */
    boolean deleteDepartment(Integer id);

    /**
     * 批量删除部门
     *
     * @param ids 部门ID列表
     * @return 删除的部门数量
     */
    int batchDeleteDepartments(List<Integer> ids);

    // ==================== 查询操作 ====================

    /**
     * 根据 ID 查询部门
     *
     * @param id 部门ID
     * @return 部门对象，如果不存在返回 null
     */
    DepartmentDto getDepartmentById(Integer id);

    /**
     * 根据部门编码查询
     *
     * @param code 部门编码
     * @return 部门对象
     */
    DepartmentDto getDepartmentByCode(String code);

    /**
     * 查询所有部门（列表结构）
     *
     * 返回的是平铺的列表，不是树形结构
     * 例如：[{技术部}, {市场部}, {前端组}, {后端组}]
     *
     * @return 所有部门列表
     */
    List<DepartmentDto> listAllDepartments();

    /**
     * 查询部门树形结构
     *
     * 返回树形结构，便于前端展示
     * 例如：
     * [
     *   {
     *     id: 1,
     *     name: "总公司",
     *     children: [
     *       { id: 2, name: "技术部", children: [...] },
     *       { id: 3, name: "市场部", children: [] }
     *     ]
     *   }
     * ]
     *
     * @return 部门树形列表
     */
    List<DepartmentDto> getDepartmentTree();

    /**
     * 根据父部门ID查询子部门
     *
     * @param parentId 父部门ID，null 表示查询顶级部门
     * @return 子部门列表
     */
    List<DepartmentDto> listDepartmentsByParentId(Integer parentId);

    // ==================== 统计操作 ====================

    /**
     * 统计部门总数
     *
     * @return 部门总数
     */
    Long countDepartments();

    /**
     * 统计子部门数量
     *
     * @param parentId 父部门ID
     * @return 子部门数量
     */
    Long countDepartmentsByParentId(Integer parentId);
}