package com.enterprisesystem.babysecure.service.impl;

import com.enterprisesystem.babysecure.mapper.DepartmentMapper;
import com.enterprisesystem.babysecure.model.dto.DepartmentDto;
import com.enterprisesystem.babysecure.model.entity.DepartmentEntity;
import com.enterprisesystem.babysecure.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门 Service 实现类
 *
 * 【@Service 注解的作用】
 * - 告诉 Spring Boot 这是一个业务逻辑组件
 * - Spring Boot 会自动扫描并注册这个类
 * - 可以通过 @Resource 或 @Autowired 在其他类中注入
 *
 * 【implements 关键字】
 * - 表示实现 DepartmentService 接口
 * - 必须实现接口中定义的所有方法
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    /**
     * 注入 DepartmentMapper
     *
     * @Resource：Java 标准注解，按名称注入
     * @Autowired：Spring 注解，按类型注入
     *
     * 这里使用 @Resource，效果和 @Autowired 类似
     * Spring Boot 会自动将 DepartmentMapper 的实现类注入进来
     */
    @Resource
    private DepartmentMapper departmentMapper;

    // ==================== 工具方法 ====================

    /**
     * Entity 转换为 DTO
     *
     * 为什么需要转换？
     * - Entity 用于数据库交互
     * - DTO 用于前后端数据传输
     * - 两者职责不同，应该分开
     *
     * @param entity 实体对象
     * @return DTO 对象
     */
    private DepartmentDto entityToDto(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }
        DepartmentDto dto = new DepartmentDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setParentId(entity.getParentId());
        dto.setStatus(entity.getStatus());
        dto.setSortOrder(entity.getSortOrder());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }

    /**
     * DTO 转换为 Entity
     *
     * @param dto DTO 对象
     * @return 实体对象
     */
    private DepartmentEntity dtoToEntity(DepartmentDto dto) {
        if (dto == null) {
            return null;
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setStatus(dto.getStatus());
        entity.setSortOrder(dto.getSortOrder());
        entity.setCreateTime(dto.getCreateTime());
        entity.setUpdateTime(dto.getUpdateTime());
        return entity;
    }

    // ==================== 增删改操作实现 ====================

    /**
     * 添加部门
     */
    @Override
    public DepartmentDto addDepartment(DepartmentDto departmentDto) {
        // 1. 校验部门编码是否已存在
        DepartmentEntity existEntity = departmentMapper.selectByCode(departmentDto.getCode());
        if (existEntity != null) {
            throw new RuntimeException("部门编码【" + departmentDto.getCode() + "】已存在");
        }

        // 2. 如果有父部门，校验父部门是否存在
        if (departmentDto.getParentId() != null) {
            DepartmentEntity parentEntity = departmentMapper.selectById(departmentDto.getParentId());
            if (parentEntity == null) {
                throw new RuntimeException("父部门不存在");
            }
        }

        // 3. DTO 转换为 Entity
        DepartmentEntity entity = dtoToEntity(departmentDto);

        // 4. 设置默认值
        if (entity.getStatus() == null) {
            entity.setStatus(1); // 默认启用
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0); // 默认排序
        }

        // 5. 保存到数据库
        int result = departmentMapper.insert(entity);

        // 6. 判断是否成功
        if (result > 0) {
            // insert 后，entity.getId() 会自动获得数据库生成的 ID
            return entityToDto(entity);
        } else {
            throw new RuntimeException("添加部门失败");
        }
    }

    /**
     * 更新部门信息
     */
    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto) {
        // 1. 校验部门是否存在
        if (departmentDto.getId() == null) {
            throw new RuntimeException("部门ID不能为空");
        }
        DepartmentEntity existEntity = departmentMapper.selectById(departmentDto.getId());
        if (existEntity == null) {
            throw new RuntimeException("部门不存在");
        }

        // 2. 如果修改了编码，校验新编码是否重复
        if (!existEntity.getCode().equals(departmentDto.getCode())) {
            DepartmentEntity codeEntity = departmentMapper.selectByCode(departmentDto.getCode());
            if (codeEntity != null) {
                throw new RuntimeException("部门编码【" + departmentDto.getCode() + "】已存在");
            }
        }

        // 3. DTO 转换为 Entity
        DepartmentEntity entity = dtoToEntity(departmentDto);

        // 4. 更新数据库
        int result = departmentMapper.update(entity);

        // 5. 判断是否成功
        if (result > 0) {
            return entityToDto(entity);
        } else {
            throw new RuntimeException("更新部门失败");
        }
    }

    /**
     * 删除部门
     */
    @Override
    public boolean deleteDepartment(Integer id) {
        // 1. 校验部门是否存在
        if (id == null) {
            throw new RuntimeException("部门ID不能为空");
        }
        DepartmentEntity existEntity = departmentMapper.selectById(id);
        if (existEntity == null) {
            throw new RuntimeException("部门不存在");
        }

        // 2. 检查是否有子部门
        Long childCount = departmentMapper.countByParentId(id);
        if (childCount > 0) {
            throw new RuntimeException("该部门下有子部门，不能删除");
        }

        // 3. 删除数据库记录
        int result = departmentMapper.deleteById(id);

        // 4. 返回是否成功
        return result > 0;
    }

    /**
     * 批量删除部门
     */
    @Override
    public int batchDeleteDepartments(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        // TODO: 实际项目中应该检查每个部门是否有子部门或员工
        // 这里简化处理，直接批量删除
        return departmentMapper.deleteByIds(ids);
    }

    // ==================== 查询操作实现 ====================

    /**
     * 根据 ID 查询部门
     */
    @Override
    public DepartmentDto getDepartmentById(Integer id) {
        DepartmentEntity entity = departmentMapper.selectById(id);
        return entityToDto(entity);
    }

    /**
     * 根据部门编码查询
     */
    @Override
    public DepartmentDto getDepartmentByCode(String code) {
        DepartmentEntity entity = departmentMapper.selectByCode(code);
        return entityToDto(entity);
    }

    /**
     * 查询所有部门（列表结构）
     */
    @Override
    public List<DepartmentDto> listAllDepartments() {
        List<DepartmentEntity> entities = departmentMapper.selectAll();
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * 查询部门树形结构
     *
     * 这是一个经典的递归算法，将平铺的列表转换为树形结构
     */
    @Override
    public List<DepartmentDto> getDepartmentTree() {
        // 1. 查询所有部门
        List<DepartmentEntity> allEntities = departmentMapper.selectAll();
        List<DepartmentDto> allDtos = allEntities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        // 2. 找出所有顶级部门（parentId 为 null 的部门）
        List<DepartmentDto> rootDepartments = allDtos.stream()
                .filter(dto -> dto.getParentId() == null)
                .collect(Collectors.toList());

        // 3. 递归构建树形结构
        for (DepartmentDto root : rootDepartments) {
            buildChildrenTree(root, allDtos);
        }

        // 4. 返回顶级部门列表
        return rootDepartments;
    }

    /**
     * 递归构建子部门树
     *
     * @param parent 父部门
     * @param allDtos 所有部门列表
     */
    private void buildChildrenTree(DepartmentDto parent, List<DepartmentDto> allDtos) {
        // 找出当前父部门的所有子部门
        List<DepartmentDto> children = allDtos.stream()
                .filter(dto -> parent.getId().equals(dto.getParentId()))
                .collect(Collectors.toList());

        // 如果有子部门
        if (!children.isEmpty()) {
            // 设置子部门列表
            parent.setChildren(children);

            // 递归处理每个子部门
            for (DepartmentDto child : children) {
                buildChildrenTree(child, allDtos);
            }
        }
    }

    /**
     * 根据父部门ID查询子部门
     */
    @Override
    public List<DepartmentDto> listDepartmentsByParentId(Integer parentId) {
        List<DepartmentEntity> entities = departmentMapper.selectByParentId(parentId);
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // ==================== 统计操作实现 ====================

    /**
     * 统计部门总数
     */
    @Override
    public Long countDepartments() {
        return departmentMapper.count();
    }

    /**
     * 统计子部门数量
     */
    @Override
    public Long countDepartmentsByParentId(Integer parentId) {
        return departmentMapper.countByParentId(parentId);
    }
}