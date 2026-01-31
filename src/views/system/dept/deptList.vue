<template>
    <div>
        <el-main>
            <!--查询表单-->
            <el-form :inline="true" size="small" width="80%">
                <el-form-item label="部门名称">
                    <el-input v-model="deptModel.deptName" placeholder="请输入部门名称" clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button>
                    <el-button type="warning" icon="el-icon-refresh" @click="resetForm">重置筛选</el-button>
                    <el-button type="success" icon="el-icon-plus" @click="handleCreate">新增</el-button>
                </el-form-item>
            </el-form>
            <!--查询表单结束-->
            <!--数据表格-->
            <el-table :data="tableData" stripe style="width: 100%" border row-key="id"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" default-expand-all>
                <el-table-column prop="deptName" label="部门名称"></el-table-column>
                <el-table-column prop="parentName" label="上级部门"></el-table-column>
                <el-table-column prop="phone" label="部门电话"></el-table-column>
                <el-table-column prop="address" label="地址"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--数据表格结束-->
            <!--对话框-->
            <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
                <el-form :model="saveDeptModel" label-width="100px" ref="form" :rules="rules">
                    <el-form-item label="上级部门" prop="parentName">
                        <el-input v-model="saveDeptModel.parentName" placeholder="请输入上级部门" :readonly="true"
                            @click.native="selectDept"></el-input>
                    </el-form-item>
                    <el-form-item label="部门名称" prop="deptName">
                        <el-input v-model="saveDeptModel.deptName" placeholder="请输入部门名称"></el-input>
                    </el-form-item>
                    <el-form-item label="部门电话" prop="phone">
                        <el-input v-model="saveDeptModel.phone" placeholder="请输入部门电话"></el-input>
                    </el-form-item>
                    <el-form-item label="地址" prop="address">
                        <el-input v-model="saveDeptModel.address" placeholder="请输入地址"></el-input>
                    </el-form-item>
                    <el-form-item label="部门排序" prop="orderNum">
                        <el-input-number v-model="saveDeptModel.orderNum" placeholder="请输入部门排序"></el-input-number>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary"
                        @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
                </div>
            </el-dialog>
            <!--对话框结束-->
            <!--部门树对话框-->
            <el-dialog title="选择上级部门" :visible.sync="dialogTreeVisible" width="30%">
                <el-tree :data="treeData" :props="defaultProps" @node-click="handleNodeClick" default-expand-all
                    empty-text="未获取部门数据" :highlight-current="true"></el-tree>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="closeTree">取消</el-button>
                    <el-button type="primary" @click="handleTreeConfirm">确定</el-button>
                </div>
            </el-dialog>
            <!--部门树对话框结束-->
        </el-main>
    </div>
</template>
<script>
import deptApi from '@/api/dept.js';
export default {
    data() {
        return {
            deptModel: {}, // 提交的部门对象
            tableData: [], // 表格数据
            saveDeptModel: {}, // 保存的部门对象
            dialogFormVisible: false, // 对话框显示控制
            dialogStatus: '', // 对话框状态 create | update
            titleMap: {
                create: '新增部门',
                update: '修改部门'
            },
            rules: {
                parentName: [
                    { required: true, message: '请输入上级部门名称', trigger: 'blur' }
                ],
                deptName: [
                    { required: true, message: '请输入部门名称', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入部门电话', trigger: 'blur' }
                ],
                address: [
                    { required: true, message: '请输入地址', trigger: 'blur' }
                ],
                orderNum: [
                    { required: true, message: '请输入部门排序', trigger: 'blur' }
                ]
            },
            dialogTreeVisible: false,
            defaultProps: {
                children: 'children', // 返回的子级数据
                label: 'deptName' // 显示的属性名
            },
            treeData: [] // 部门树数据
        }
    },
    created() {
        this.search();
    },
    methods: {
        // 查询
        search() {
            deptApi.search(this.deptModel).then(res => {
                this.tableData = res.data;
            });
        },
        // 响应查询按钮
        onSubmit() {
            this.search();
        },
        // 响应重置按钮
        resetForm() {
            this.deptModel = {};
            this.search();
        },
        // 响应新增按钮
        handleCreate() {
            this.saveDeptModel = {};
            this.dialogStatus = 'create';
            this.dialogFormVisible = true;
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            });
        },
        // 响应编辑按钮
        handleUpdate(row) {
            this.saveDeptModel = Object.assign({}, row);
            this.dialogStatus = 'update';
            this.dialogFormVisible = true;
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            });
        },
        // 响应删除按钮
        async handleDelete(row) {
            let res = await deptApi.hasChildren(row.id)
            if (res.message === '有') {
                this.$message({
                    message: '请先删除子部门',
                    type: 'warning'
                });
            } else {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    const res = await deptApi.delete(row.id);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.search();
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                })
            }
        },
        // 新增数据
        createData() {
            this.$refs['form'].validate((valid) => {
                if (!valid) {
                    return false;
                } else {
                    deptApi.save(this.saveDeptModel).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: '新增成功!'
                            })
                            this.dialogFormVisible = false
                            this.search()
                        }
                    });
                }
            });
        },
        // 修改数据
        updateData() {
            this.$refs['form'].validate(async (valid) => {
                if (!valid) {
                    return false;
                } else {
                    const res = await deptApi.update(this.saveDeptModel);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '修改成功!'
                        })
                        this.dialogFormVisible = false
                        this.search()
                    }
                }
            });
        },
        // 查询部门树
        selectDept() {
            this.dialogTreeVisible = true;
            deptApi.selectTree().then(res => {
                if (res.success) {
                    this.treeData = res.data;
                }
            });
        },
        // 树节点点击
        handleNodeClick(data) {
            this.saveDeptModel.pid = data.id;
            this.saveDeptModel.parentName = data.deptName;
        },
        // 部门树对话框确定
        handleTreeConfirm() {
            this.dialogTreeVisible = false;
            // 确定后清除验证的错误信息
            this.$refs.form.clearValidate('parentName');
        },
        // 关闭树形对话框
        closeTree() {
            this.dialogTreeVisible = false;
            // 取消可以清空输入框数据
            this.saveDeptModel.pid = -1;
            this.saveDeptModel.parentName = '';
        },
    }
}
</script>
<style></style>