<template>
    <div>
        <el-main>
            <!--顶部按钮-->
            <el-button type="success" @click="handleCreate">新增</el-button>
            <!--顶部按钮结束-->
            <!--数据表格-->
            <el-table :data="tableData" style="width: 100%; margin-top: 20px;" border stripe default-expand-all
                row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" :height="height">
                <el-table-column prop="permissionLabel" label="菜单名称" width="180" />
                <el-table-column prop="parentLabel" label="上级菜单" width="180" />
                <el-table-column prop="permissionType" label="菜单类型" width="180">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.permissionType === 0" type="warning">根目录</el-tag>
                        <el-tag v-else-if="scope.row.permissionType === 1" type="success">菜单</el-tag>
                        <el-tag v-else-if="scope.row.permissionType === 2">按钮</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="routeName" label="路由名称" width="180" />
                <el-table-column prop="routeUrl" label="路由地址" width="180" />
                <el-table-column prop="routePath" label="菜单路径" width="180" />
                <el-table-column prop="icon" label="菜单图标" width="158">
                    <template slot-scope="scope">
                        <i :class="scope.row.icon" v-if="scope.row.icon.indexOf('el-icon') !== -1"></i>
                        <svg-icon :icon-class="scope.row.icon" v-else></svg-icon>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">修改</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--数据表格结束-->
            <!--对话框-->
            <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
                <el-form :model="savePermissionModel" label-width="100px" ref="form" :rules="rules">
                    <el-form-item label="上级菜单" prop="parentLabel">
                        <el-input v-model="savePermissionModel.parentLabel" placeholder="请输入上级菜单" :readonly="true"
                            @click.native="selectPermission"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单名称" prop="permissionLabel">
                        <el-input v-model="savePermissionModel.permissionLabel" placeholder="请输入菜单名称"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单类型" prop="permissionType">
                        <el-radio-group v-model="savePermissionModel.permissionType">
                            <el-radio-button :label="0">根目录</el-radio-button>
                            <el-radio-button :label="1">菜单</el-radio-button>
                            <el-radio-button :label="2">按钮</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="权限编码" prop="permissionCode">
                        <el-input v-model="savePermissionModel.permissionCode" placeholder="请输入权限编码"></el-input>
                    </el-form-item>
                    <el-form-item label="路由地址" prop="routeUrl" v-if="savePermissionModel.permissionType != 2">
                        <el-input v-model="savePermissionModel.routeUrl" placeholder="请输入路由地址"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单图标" prop="icon">
                        <el-select v-model="savePermissionModel.icon" placeholder="请选择图标">
                            <el-option v-for="item in icons" :key="item.value" :label="item.label" :value="item.value">
                                <span style="float: left">{{ item.label }}</span>
                                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
                            </el-option>
                        </el-select>
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
            <el-dialog title="选择部门" :visible.sync="dialogTreeVisible" width="30%">
                <el-tree ref="tree" :data="permissionTree" :props="defaultProps" node-key="id" default-expand-all
                    @node-click="handleNodeClick">
                </el-tree>
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
import permissionApi from '@/api/permission.js'
export default {
    data() {
        return {
            // 数据表格相关属性
            tableData: [],
            height: window.innerHeight - 200, // 表格高度

            // 菜单相关属性
            titleMap: {
                create: '新增菜单',
                update: '修改菜单'
            },
            savePermissionModel: {},
            dialogFormVisible: false,
            dialogStatus: '', // 对话框状态 create | update
            rules: {
                parentLabel: [{ required: true, message: '请输入上级菜单', trigger: 'blur' }],
                permissionLabel: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
                permissionType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
                permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
                routeUrl: [{ required: true, message: '请输入路由地址', trigger: 'blur' }],
            },
            icons: [{
                value: 'component',
                label: '根目录'
            }, {
                value: 'guide',
                label: '菜单'
            }, {
                value: 'el-icon-plus',
                label: '新增'
            }, {
                value: 'el-icon-delete',
                label: '删除'
            }, {
                value: 'el-icon-edit',
                label: '修改'
            }, {
                value: 'el-icon-search',
                label: '查询'
            }],
            dialogTreeVisible: false,
            permissionTree: [],
            defaultProps: {
                children: 'children',
                label: 'permissionLabel'
            }
        }
    },
    created() {
        this.search()
    },
    methods: {
        async search() {
            const res = await permissionApi.list()
            if (res.success) {
                this.tableData = res.data
            }
        },
        handleCreate() {
            this.savePermissionModel = {}
            this.dialogStatus = 'create'
            this.dialogFormVisible = true
            this.$nextTick(() => {
                this.$refs['form'].clearValidate()
            })
        },
        handleUpdate(row) {
            this.savePermissionModel = Object.assign({}, row)
            this.dialogStatus = 'update'
            this.dialogFormVisible = true
            this.$nextTick(() => {
                this.$refs['form'].clearValidate()
            })
        },
        async selectPermission() {
            this.dialogTreeVisible = true
            const res = await permissionApi.selectTree()
            if (res.success) {
                this.permissionTree = res.data
            }
        },
        handleNodeClick(data) {
            this.savePermissionModel.pid = data.id
            this.savePermissionModel.parentLabel = data.permissionLabel
            console.log(this.savePermissionModel)
        },
        closeTree() {
            this.dialogTreeVisible = false
            this.savePermissionModel.pid = -1
            this.savePermissionModel.parentLabel = ''
        },
        handleTreeConfirm() {
            this.dialogTreeVisible = false
            this.$refs['form'].clearValidate('parentLabel')
        },
        async handleDelete(row) {
            let res = await permissionApi.hasChildren(row.id)
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
                    const res = await permissionApi.delete(row.id);
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
        createData() {
            this.$refs['form'].validate((valid) => {
                if (valid) {
                    permissionApi.save(this.savePermissionModel).then(res => {
                        if (res.success) {
                            this.$message({
                                type: 'success',
                                message: '新增成功!'
                            })
                            this.dialogFormVisible = false
                            this.search()
                        }
                    })
                }
            });
        },
        updateData() {
            this.$refs['form'].validate(async (valid) => {
                if (valid) {
                    const res = await permissionApi.update(this.savePermissionModel)
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
    }
}
</script>
<style></style>