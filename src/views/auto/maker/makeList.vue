<template>
  <div>
    <el-main>
      <!--查询表单-->
      <el-form :inline="true" size="small" width="80%">
        <el-form-item label="厂商名称">
          <el-input v-model="makerModel.name" placeholder="请填写厂商名称"></el-input>
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
      <el-table :data="tableData" style="width: 100%" margin-bottom:20px; border stripe
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="60">
        </el-table-column>
        <el-table-column type="index" label="序号" width="60">
          <template slot-scope="scope">{{ (start - 1) * size + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="name" label="厂商名称">
        </el-table-column>
        <el-table-column prop="orderLetter" label="排序字母">
        </el-table-column>
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
        @current-change="search" :total="total">
      </el-pagination>
      <!--分页组件结束-->

      <!--对话框-->
      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible">
        <el-form :model="saveMakerModel" label-width="100px" ref="form" :rules="rules">
          <el-form-item label="厂商名称" prop="name">
            <el-input v-model="saveMakerModel.name"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确 定</el-button>
        </div>
      </el-dialog>
      <!--对话框结束-->
    </el-main>
  </div>
</template>
<script>
import makerApi from '@/api/auto-maker.js'

export default {
  data() {
    return {
      makerModel: {}, // 查询时，表单提交的对象
      start: 1,
      size: 5,
      total: 0,
      tableData: [], // 表格数据
      multipleSelection: [], // 对选框选中的数据id数组
      dialogFormVisible: false, // 对话框是否可见
      saveMakerModel: {}, // 新增或编辑时，保存的厂商对象
      dialogStatus: '', // 对话框状态，新增或编辑
      titleMap: {
        create: '新增厂商',
        update: '编辑厂商'
      }, // 对话框标题
      rules: {
        name: [
          { required: true, message: '请输入厂商名称', trigger: 'blur' }
        ]
      } // 表单校验规则，要求必须填写厂商名称
    }
  },
  created() {
    this.search()
  },
  methods: {
    // 查询
    async search(start = 1) {
      this.start = start
      const res = await makerApi.search(this.start, this.size, this.makerModel)
      if (res.success) {
        this.tableData = res.data.records
        this.total = res.data.total
      }
    },
    // 按钮查询，因为是异步请求，不能直接调用search方法
    onSubmit() {
      this.search()
    },
    // 重置表单数据
    resetForm() {
      this.makerModel = {}
      this.search()
    },
    // 响应新增按钮
    handleCreate() {
      this.dialogStatus = 'create'
      this.saveMakerModel = {}
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      });
    },
    // 响应修改按钮
    handleUpdate(row) {
      this.dialogStatus = 'update'
      this.saveMakerModel = Object.assign({}, row)
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['form'].clearValidate();
      });
    },
    // 响应删除按钮
    handleDelete(row) {
      this.$confirm('是否确认删除该厂商？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        makerApi.delete(row.id).then(res => {
          if (res.success) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.search()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 新增
    createData() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          makerApi.save(this.saveMakerModel).then(res => {
            if (res.success) {
              this.$message({
                type: 'success',
                message: '新增成功!'
              })
              this.dialogFormVisible = false
              this.search()
            }
          })
        } else {
          return false
        }
      })
    },
    // 修改
    updateData() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          makerApi.update(this.saveMakerModel).then(res => {
            if (res.success) {
              this.$message({
                type: 'success',
                message: '修改成功!'
              })
              this.dialogFormVisible = false
              this.search()
            }
          })
        } else {
          return false
        }
      })
    },
    // 删除选中
    deleteSelect() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请选择要删除的厂商',
          type: 'warning'
        })
        return
      }
      this.$confirm('是否确认删除选中的厂商？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        makerApi.delete(this.multipleSelection).then(res => {
          if (res.success) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.search()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleSelectionChange(val) {
      // 加入前需要清空多选数组，否则会重复添加
      this.multipleSelection = []
      val.forEach(element => {
        this.multipleSelection.push(element.id)
      });
    },
  }
}
</script>
<style></style>