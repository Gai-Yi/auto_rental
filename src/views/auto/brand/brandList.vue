<template>
    <div>
        <el-main>
            <!--查询表单-->
            <el-form :model="brandModel" :inline="true" size="small" width="80%">
                <el-form-item label="厂商名称">
                    <el-select v-model="brandModel.makerId" placeholder="请选择厂商名称" style="width: 200px;">
                        <el-option v-for="item in makerOptions" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="品牌名称">
                    <el-input v-model="brandModel.brandName" placeholder="请填写品牌名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="onSubmit">查询</el-button>
                    <el-button type="warning" icon="el-icon-refresh" @click="resetForm">重置筛选</el-button>
                    <el-button type="success" icon="el-icon-plus" @click="handleCreate">新增</el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="deleteSelect">删除选中</el-button>
                </el-form-item>
            </el-form>
            <!--查询表单结束-->
            <!--数据表格-->
            <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column type="index" label="序号" width="60">
                    <template slot-scope="scope">{{ (start - 1) * size + scope.$index + 1 }}</template>
                </el-table-column>
                <el-table-column prop="makerName" label="所属厂商"></el-table-column>
                <el-table-column prop="brandName" label="品牌名称"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--数据表格结束-->
            <!--分页组件-->
            <el-pagination background layout="total, prev, pager, next, jumper" :current-page="start" :page-size="size"
                @current-change="search" :total="total" style="text-align: center;">
            </el-pagination>
            <!--分页组件结束-->
            <!--对话框-->
            <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
                <el-form :model="saveBrandModel" label-width="100px" ref="form" :rules="rules">
                    <el-form-item label="所属厂商" prop="makerId">
                        <el-select v-model="saveBrandModel.makerId" placeholder="请选择厂商名称" style="width: 200px;">
                            <el-option v-for="item in makerOptions" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="品牌名称" prop="brandName">
                        <el-input v-model="saveBrandModel.brandName"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary"
                        @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
                </div>
            </el-dialog>
            <!--对话框结束-->
        </el-main>
    </div>
</template>
<script>
import brandApi from '@/api/auto-brand.js';
import makerApi from '@/api/auto-maker.js';
export default {
    data() {
        return {
            brandModel: {}, // 提交的品牌对象
            makerOptions: [], // 厂商选项的集合
            tableData: [], // 表格数据
            start: 1,
            size: 5,
            total: 0,
            multipleSelection: [],
            saveBrandModel: {}, // 新增或修改时提交的对象
            dialogStatus: '',
            dialogFormVisible: false,
            titleMap: {
                create: '新增品牌',
                update: '修改品牌'
            },
            rules: {
                makerId: [
                    { required: true, message: '请选择所属厂商', trigger: 'blur' }
                ],
                brandName: [
                    { required: true, message: '请填写品牌名称', trigger: 'blur' }
                ]
            }
        }
    },
    created() {
        this.loadMakerOptions();
        this.search();
    },
    methods: {
        // 加载厂商下拉选项
        async loadMakerOptions() {
            const res = await makerApi.selectAll();
            this.makerOptions = res.data;
        },
        // 查询
        async search(start = 1) {
            // 不传参数默认从第一页开始查询
            this.start = start;
            const res = await brandApi.search(this.start, this.size, this.brandModel);
            if (res.success) {
                this.tableData = res.data.records;
                this.total = res.data.total;
            }
        },
        // 获取选中数据的id数组
        handleSelectionChange(val) {
            this.multipleSelection = [];
            val.forEach(item => {
                this.multipleSelection.push(item.id);
            });
        },
        // 查询
        onSubmit() {
            this.search();
        },
        // 重置
        resetForm() {
            this.brandModel = {};
            this.search();
        },
        // 响应新增按钮
        handleCreate() {
            this.dialogStatus = 'create';
            this.saveBrandModel = {};
            this.dialogFormVisible = true;
            // 清空表单验证，防止上次验证未通过的提示信息还在显示
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            });
        },
        // 响应修改按钮
        handleUpdate(row) {
            this.dialogStatus = 'update';
            this.dialogFormVisible = true;
            this.saveBrandModel = Object.assign({}, row);
            this.$nextTick(() => {
                this.$refs['form'].clearValidate();
            });
        },
        // 新增
        createData() {
            this.$refs['form'].validate(async (valid) => {
                if (valid) {
                    const res = await brandApi.save(this.saveBrandModel);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '新增成功!'
                        })
                        this.dialogFormVisible = false
                        this.search();
                    }
                }
            });
        },
        // 修改
        updateData() {
            this.$refs['form'].validate(async (valid) => {
                if (valid) {
                    const res = await brandApi.update(this.saveBrandModel);
                    if (res.success) {
                        this.$message({
                            type: 'success',
                            message: '修改成功!'
                        })
                        this.dialogFormVisible = false
                        this.search();
                    }
                }
            });
        },
        // 删除选中
        deleteSelect() {
            if (this.multipleSelection.length == 0) {
                this.$message({
                    type: 'warning',
                    message: '请选择要删除的项!'
                })
            } else {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    brandApi.delete(this.multipleSelection).then(res => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        })
                        this.search()
                    })
                }).catch(() => {
                });
            }
        }
    }
}
</script>
<style></style>