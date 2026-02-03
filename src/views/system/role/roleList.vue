<template>
    <div>
        <el-main>
            <!--查询表单-->
            <el-form :model="roleModel" :inline="true" size="small" width="80%">
                <el-form-item label="角色名称">
                    <el-input v-model="roleModel.roleName" placeholder="请输入角色名称" clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button>
                    <el-button type="warning" icon="el-icon-refresh" @click="resetForm">重置</el-button>
                    <el-button type="success" icon="el-icon-plus" @click="handleCreate">新增</el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="deleteSelect">删除</el-button>
                </el-form-item>
            </el-form>
            <!--查询表单结束-->
            <!--数据表格-->
            <el-table :data="tableData" border style="width: 100%; margin-top: 20px" row-key="id"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="序号" width="80">
                    <template slot-scope="scope">
                        {{ (start - 1) * size + scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="roleName" label="角色名称"></el-table-column>
                <el-table-column prop="roleCode" label="角色标识"></el-table-column>
                <el-table-column prop="remark" label="角色描述"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" icon="el-icon-edit"
                            @click="handleUpdate(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" icon="el-icon-delete"
                            @click="handleDelete(scope.row)">删除</el-button>
                        <el-button type="success" size="mini" icon="el-icon-setting"
                            @click="handlePermission(scope.row)">分配权限</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--数据表格结束-->
            <!--分页组件-->
            <el-pagination :current-page="start" background :page-size="size" :total="total"
                layout="total, prev, pager, next, jumper" :current-change="search">
            </el-pagination>
            <!--分页组件结束-->
            <!--对话框-->
            <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
                <el-form :model="saveRoleModel" label-width="100px" ref="form" :rules="rules">
                    <el-form-item label="角色名称" prop="roleName">
                        <el-input v-model="saveRoleModel.roleName"></el-input>
                    </el-form-item>
                    <el-form-item label="角色标识" prop="roleCode">
                        <el-input v-model="saveRoleModel.roleCode"></el-input>
                    </el-form-item>
                    <el-form-item label="角色描述" prop="remark">
                        <el-input type="textarea" v-model="saveRoleModel.remark"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary"
                        @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
                </div>
            </el-dialog>
            <!--对话框结束-->
            <!--分配权限对话框-->
            <el-dialog :title="treeTitle" :visible.sync="dialogTreeVisible" width="30%">
                <el-tree :data="treeData" show-checkbox node-key="id" :default-checked-keys="checkedKeys"
                    :props="defaultProps" default-expand-all ref="permissionTree">
                </el-tree>
                <div slot="footer" class="dialog-footer">
                        <el-button @click="clearSelect">取消</el-button>
                        <el-button type="primary" @click="savePermission">确定</el-button>
                    </div>
            </el-dialog>
            <!--分配权限对话框结束-->
        </el-main>
    </div>
</template>
<script>
import roleApi from "@/api/auth-role.js"
import { valid } from "mockjs";

export default {
    data() {
        return {
            roleModel: {},
            tableData: [],
            start: 1,
            size: 5,
            total: 0,
            dialogFormVisible: false,
            dialogStatus: '',
            titleMap: {
                create: '新增角色',
                update: '修改角色'
            },
            saveRoleModel: {},
            rules: {
                roleName: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' }
                ],
                roleCode: [
                    { required: true, message: '请输入角色编码', trigger: 'blur' }
                ]
            },
            multipleSelection: [],
            treeTitle: '',
            dialogTreeVisible: false,
            treeData: [],
            checkedKeys: [],
            defaultProps: {
                children: 'children',
                label: 'permissionLabel'
            },
            roleId: 0
        }
    },
    created() {
        this.search();
    },
    methods: {
        async search(start = 1) {
            this.start = start;
            this.roleModel.createId = this.$store.getters.userId;
            const res = await roleApi.search(this.start, this.size, this.roleModel)
            if (res.success) {
                this.tableData = res.data.records;
                this.total = res.data.total;
            }
        },
        onSubmit() {
            this.search();
        },
        resetForm() {
            this.roleModel = {};
            this.search()
        },
        deleteSelect() {
            if (this.multipleSelection.length === 0) {
                this.$message({
                    messgae: "请选择要删除的数据",
                    type: "warning"
                })
            } else {
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(async () => {
                    const res = await roleApi.delete(this.multipleSelection);
                    if (res.success) {
                        this.$message({
                            message: "已成功删除没有绑定用户的角色!",
                            type: "success"
                        });
                        this.search();
                    }
                })
            }
        },
        handleCreate() {
            this.dialogFormVisible = true;
            this.dialogStatus = 'create';
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            })
        },
        handleUpdate(row) {
            this.dialogFormVisible = true;
            this.dialogStatus = 'update';
            this.saveRoleModel = Object.assign({}, row);
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            })
        },
        handleDelete(row) {
            roleApi.hasUser(row.id).then(res => {
                if (res.data === '有') {
                    this.$message({
                        type: 'warning',
                        message: '该角色下有用户，不能删除!'
                    })
                } else {
                    this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(async () => {
                        const res = await roleApi.delete(row.id);
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            this.search();
                        }
                    }).catch(() => { });
                }
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = [];
            val.forEach(item => {
                this.multipleSelection.push(item.id);
            })
        },
        createData() {
            this.$refs['form'].validate(async () => {
                if (valid) {
                    this.saveRoleModel.createId = this.$store.getters.userId;
                    const res = await roleApi.save(this.saveRoleModel);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '新增成功!'
                        })
                        this.dialogFormVisible = false
                        this.search();
                    }
                }
            })
        },
        updateData() {
            this.$refs['form'].validate(async () => {
                if (valid) {
                    const res = await roleApi.update(this.saveRoleModel);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '新增成功!'
                        })
                        this.dialogFormVisible = false
                        this.search();
                    }
                }
            })
        },
        async handlePermission(row) {
            this.treeTitle = `${row.roleName}权限分配`
            this.dialogTreeVisible = true
            this.roleId = row.id
            const res = await roleApi.getPermissionTree(this.$store.getters.userId, this.roleId);
            if (res.success) {
                this.treeData = res.data.permissionList;
                this.checkedKeys = res.data.checkedList;
                console.log(res.data.checkedList);
            }
        },
        clearSelect() {
            this.dialogTreeVisible = false;
        },
        async savePermission() {
            let permissionIds = this.$refs['permissionTree'].getCheckedKeys();
            // 或者
            // let permissionIds = this.$refs.permissionTree.getCheckedKeys();
            const res = await roleApi.assignPermission(this.roleId, permissionIds);
            if (res.success) {
                this.$message({
                    message: '分配成功！',
                    type: 'success'
                });
                this.dialogTreeVisible = false
            }
        }
    }
}
</script>
<style></style>