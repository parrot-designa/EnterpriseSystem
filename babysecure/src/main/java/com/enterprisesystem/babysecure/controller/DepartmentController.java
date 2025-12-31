package com.enterprisesystem.babysecure.controller;

import com.enterprisesystem.babycommon.annotation.ApiExceptionHandler;
import com.enterprisesystem.babycommon.constant.ApiConstants;
import com.enterprisesystem.babycommon.entity.APIResult;
import com.enterprisesystem.babysecure.model.dto.DepartmentDto;
import com.enterprisesystem.babysecure.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门管理 Controller
 *
 * 【@RestController 注解】
 * - 告诉 Spring Boot 这是一个 REST 控制器
 * - 自动将返回的对象转换为 JSON 格式
 * - 相当于 @Controller + @ResponseBody 的组合
 *
 * 【@RequestMapping 注解】
 * - 定义这个 Controller 的基础路径
 * - 所有方法的 URL 都会拼接这个路径
 * - 例如：@RequestMapping("/api/v1/department")
 *   那么 @GetMapping("/list") 的完整路径是 /api/v1/department/list
 *
 * 【RESTful API 设计规范】
 * - POST   /api/v1/department          创建部门
 * - PUT    /api/v1/department          更新部门
 * - DELETE /api/v1/department/{id}     删除部门
 * - GET    /api/v1/department/{id}     查询单个部门
 * - GET    /api/v1/department/list     查询所有部门
 * - GET    /api/v1/department/tree     查询部门树
 */
@RequestMapping(ApiConstants.API_V1 + "/departments")
@RestController
public class DepartmentController {

    /**
     * 注入 DepartmentService
     *
     * @Resource：按名称注入，这里会注入 DepartmentServiceImpl
     * Spring Boot 会自动将 Service 实现类注入进来
     */
    @Resource
    private DepartmentService departmentService;

    // ==================== 创建和更新 ====================

    /**
     * 创建部门
     *
     * 【@PostMapping】
     * - 映射 HTTP POST 请求
     * - 完整路径：POST /api/v1/department
     *
     * 【@ApiExceptionHandler】
     * - 自定义注解，用于异常处理
     * - 如果方法抛出异常，会被全局异常处理器捕获
     *
     * 【参数接收方式】
     * - 方法参数直接写 DepartmentDto
     * - Spring Boot 会自动将 HTTP 请求体（JSON）转换为 DepartmentDto 对象
     * - 前端发送：{"code": "DEPT001", "name": "技术部"}
     * - Spring Boot 自动转换为：DepartmentDto 对象
     *
     * 【返回值包装】
     * - 使用 APIResult 包装返回结果
     * - 统一响应格式：{code, message, data, timestamp}
     *
     * @param departmentDto 部门信息
     * @return APIResult<DepartmentDto>
     *
     * 请求示例：
     * POST http://localhost:8080/api/v1/department
     * Content-Type: application/json
     * {
     *   "code": "DEPT001",
     *   "name": "技术部",
     *   "parentId": null,
     *   "status": 1,
     *   "sortOrder": 1
     * }
     */
    @PostMapping
    @ApiExceptionHandler(apiId = 1)
    public APIResult<DepartmentDto> createDepartment(DepartmentDto departmentDto) {
        // 调用 Service 层创建部门
        DepartmentDto result = departmentService.addDepartment(departmentDto);

        // 包装为统一响应格式返回
        return new APIResult<>(result);
    }

    /**
     * 更新部门信息
     *
     * 【@PutMapping】
     * - 映射 HTTP PUT 请求
     * - 完整路径：PUT /api/v1/department
     * - RESTful 规范：PUT 用于更新资源
     *
     * @param departmentDto 部门信息（必须包含 id）
     * @return APIResult<DepartmentDto>
     *
     * 请求示例：
     * PUT http://localhost:8080/api/v1/department
     * Content-Type: application/json
     * {
     *   "id": 1,
     *   "code": "DEPT001",
     *   "name": "技术研发部",
     *   "parentId": null,
     *   "status": 1,
     *   "sortOrder": 1
     * }
     */
    @PutMapping
    @ApiExceptionHandler(apiId = 2)
    public APIResult<DepartmentDto> updateDepartment(DepartmentDto departmentDto) {
        DepartmentDto result = departmentService.updateDepartment(departmentDto);
        return new APIResult<>(result);
    }

    // ==================== 删除操作 ====================

    /**
     * 根据ID删除部门
     *
     * 【@DeleteMapping】
     * - 映射 HTTP DELETE 请求
     * - 完整路径：DELETE /api/v1/department/{id}
     *
     * 【@PathVariable 注解】
     * - 从 URL 路径中获取参数
     * - 例如：DELETE /api/v1/department/5
     * - {id} 会被解析为 5
     * - @PathVariable("id") Integer id 将 5 赋值给 id 变量
     *
     * @param id 部门ID
     * @return APIResult<Boolean>
     *
     * 请求示例：
     * DELETE http://localhost:8080/api/v1/department/5
     */
    @DeleteMapping("/{id}")
    @ApiExceptionHandler(apiId = 3)
    public APIResult<Boolean> deleteDepartment(@PathVariable("id") Integer id) {
        boolean success = departmentService.deleteDepartment(id);
        return new APIResult<>(success);
    }

    /**
     * 批量删除部门
     *
     * 【@RequestParam 注解】
     * - 从 URL 查询参数中获取值
     * - 例如：DELETE /api/v1/department/batch?ids=1,2,3
     * - ids=1,2,3 会被解析为 List<Integer> [1, 2, 3]
     *
     * @param ids 部门ID列表，多个ID用逗号分隔
     * @return APIResult<Integer> 返回删除的数量
     *
     * 请求示例：
     * DELETE http://localhost:8080/api/v1/department/batch?ids=1,2,3
     */
    @DeleteMapping("/batch")
    @ApiExceptionHandler(apiId = 4)
    public APIResult<Integer> batchDeleteDepartments(@RequestParam("ids") List<Integer> ids) {
        int count = departmentService.batchDeleteDepartments(ids);
        return new APIResult<>(count);
    }

    // ==================== 查询操作 ====================

    /**
     * 根据ID查询单个部门
     *
     * 【@GetMapping】
     * - 映射 HTTP GET 请求
     * - 完整路径：GET /api/v1/department/{id}
     *
     * @param id 部门ID
     * @return APIResult<DepartmentDto>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/1
     */
    @GetMapping("/{id}")
    @ApiExceptionHandler(apiId = 5)
    public APIResult<DepartmentDto> getDepartmentById(@PathVariable("id") Integer id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return new APIResult<>(department);
    }

    /**
     * 查询所有部门（列表结构）
     *
     * 完整路径：GET /api/v1/department/list
     *
     * 返回的是平铺的列表，不是树形结构
     *
     * @return APIResult<List<DepartmentDto>>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/list
     */
    @GetMapping("/list")
    @ApiExceptionHandler(apiId = 6)
    public APIResult<List<DepartmentDto>> listAllDepartments() {
        List<DepartmentDto> departments = departmentService.listAllDepartments();
        return new APIResult<>(departments);
    }

    /**
     * 查询部门树形结构
     *
     * 完整路径：GET /api/v1/department/tree
     *
     * 返回树形结构，便于前端展示
     *
     * @return APIResult<List<DepartmentDto>>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/tree
     */
    @GetMapping("/tree")
    @ApiExceptionHandler(apiId = 7)
    public APIResult<List<DepartmentDto>> getDepartmentTree() {
        List<DepartmentDto> tree = departmentService.getDepartmentTree();
        return new APIResult<>(tree);
    }

    /**
     * 根据父部门ID查询子部门
     *
     * 【@RequestParam(required = false)】
     * - required = false：表示参数可选
     * - 如果不传 parentId，默认为 null
     * - 这样可以查询顶级部门
     *
     * @param parentId 父部门ID
     * @return APIResult<List<DepartmentDto>>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/children?parentId=1
     * GET http://localhost:8080/api/v1/department/children (查询顶级部门)
     */
    @GetMapping("/children")
    @ApiExceptionHandler(apiId = 8)
    public APIResult<List<DepartmentDto>> listDepartmentsByParentId(
            @RequestParam(value = "parentId", required = false) Integer parentId) {
        List<DepartmentDto> children = departmentService.listDepartmentsByParentId(parentId);
        return new APIResult<>(children);
    }

    /**
     * 根据部门编码查询
     *
     * @param code 部门编码
     * @return APIResult<DepartmentDto>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/byCode?code=DEPT001
     */
    @GetMapping("/byCode")
    @ApiExceptionHandler(apiId = 9)
    public APIResult<DepartmentDto> getDepartmentByCode(@RequestParam("code") String code) {
        DepartmentDto department = departmentService.getDepartmentByCode(code);
        return new APIResult<>(department);
    }

    // ==================== 统计操作 ====================

    /**
     * 统计部门总数
     *
     * @return APIResult<Long>
     *
     * 请求示例：
     * GET http://localhost:8080/api/v1/department/count
     */
    @GetMapping("/count")
    @ApiExceptionHandler(apiId = 10)
    public APIResult<Long> countDepartments() {
        Long count = departmentService.countDepartments();
        return new APIResult<>(count);
    }
}
