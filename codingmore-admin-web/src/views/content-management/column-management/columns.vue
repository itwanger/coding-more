<template>
  <div class="app-container">
    <el-row class="app-container" type="flex" :gutter="20">
      <!--左侧部分-->
      <el-col :span="6">
        <div class="filter-container">
          <div>
            <el-input v-model="searchTreeText" placeholder="栏目名称" @keyup.enter.native="searchTree">
              <!-- <template slot="append">111</template> -->
              <el-button slot="append" icon="el-icon-search" @click="searchTree" />
            </el-input>
          </div>
        </div>
        <div class="tree-area">
          <el-tree ref="columnTree" :filter-node-method="filterNode" :auto-expand-parent="true" :highlight-current="true" :data="treeData" node-key="termTaxonomyId" :expand-on-click-node="false" :props="treeDefaultProps" @current-change="handleCurrentChange" />
        </div>
      </el-col>
      <!--右侧部分-->
      <el-col :span="18">
        <div class="filter-container">
          <div class="text-right">
            <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
              新增
            </el-button>
            <!-- <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete">
              删除
            </el-button> -->
          </div>
        </div>
        <div class="table-container">
          <el-table ref="multipleTable" height="calc(100% - 10px)" :key="tableKey" :data="treeSelectedNode !== null ? treeSelectedNode.children : []" border fit highlight-current-row style="width: 100%;" @selection-change="handleSelectionChange">
            <el-table-column align="center" class-name="recorrect-center" type="selection" width="55px" />
            <el-table-column label="编号" prop="termTaxonomyId" align="center" width="80px">
              <!-- <template slot-scope="{row}">
              <span>{{ row.termTaxonomyId }}</span>
            </template> -->
            </el-table-column>
            <el-table-column label="栏目名称" prop="name" width="200px" />
            <el-table-column label="栏目描述" prop="description" />
            <el-table-column label="操作" align="center" width="230px" class-name="small-padding fixed-width">
              <template slot-scope="{row,$index}">
                <el-button type="primary" size="mini" @click="handleUpdate(row)">
                  编辑
                </el-button>
                <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-dialog v-loading="dialogLoading" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="columnDataModel" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <el-form-item label="栏目名称" prop="name">
          <el-input v-model="columnDataModel.name" maxlength="20" placeholder="请输入栏目名称" />
        </el-form-item>
        <el-form-item label="栏目描述">
          <el-input v-model="columnDataModel.description" :autosize="{ minRows: 4, maxRows: 6}" type="textarea" placeholder="请输入栏目描述" maxlength="1000" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllColumns, addColumn, updateColumn, deleteColumn, getOneColumn } from '@/api/columns'
import { emptyChecker } from '@/utils/validate'
import { loopExpendTree } from '@/utils/common'
import qs from 'qs'

const templateSolutions = [
  { key: '1', display_name: '方案1' },
  { key: '2', display_name: '方案2' },
  { key: '3', display_name: '方案3' },
  { key: '4', display_name: '方案4' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const templateSolutionsKeyValue = templateSolutions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ColumnsManagement',
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    templateSolutionFilter(type) {
      return templateSolutionsKeyValue[type]
    }
  },
  data() {
    return {
      tableKey: 0,
      dialogLoading: false,

      templateSolutions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,

      // 拼接的栏目树的根节点，也就是当前站点
      siteNodeOnTree: null,

      // 页面用来编辑的数据模型
      columnDataModel: {
        termTaxonomyId: undefined,
        name: '',
        attribute: '',
        description: '',
        parentId: -1
        // siteId: this.currentSiteId
      },

      // 是否显示编辑弹窗
      dialogFormVisible: false,
      // 编辑弹窗当前状态
      dialogStatus: '',
      // 编辑弹窗显示标题字典
      textMap: {
        update: '编辑栏目',
        create: '新增栏目'
      },
      // 编辑弹窗校验规则
      rules: {
        name: [{ required: true, validator: emptyChecker, message: '栏目名称不能为空', trigger: 'blur' }],
        tplPath: [{ required: true, validator: emptyChecker, message: '栏目默认概览模板不能为空', trigger: 'blur' }],
        contentTplPath: [{ required: true, validator: emptyChecker, message: '栏目默认细览模板不能为空', trigger: 'blur' }]
      },

      // 树形栏目数据
      treeData: null,
      // 当前选中树节点的数据
      treeSelectedNode: null,
      // 当前树节点过滤条件
      searchTreeText: '',
      treeDefaultProps: {
        children: 'children',
        label: 'name'
      },

      // 概览模板列表
      avaliableOutlineTemplateList: [],
      // 细览模板列表
      avaliableDetileTemplateList: []
    }
  },
  computed: {
    // currentSiteId() {
    //   return this.$store.getters.siteManagement.currentSiteId
    // },
    currentSite() {
      return this.$store.getters.siteManagement.currentSite
    }
  },
  mounted() {
    // 初始化站点节点
    this.siteNodeOnTree = {
      termTaxonomyId: 0,
      name: '全部',
      tplPath: '',
      tplDetailPath: '',
      description: '',
      parentId: -1,
      // siteId: this.currentSiteId,
      children: null
    }
    this.getTreeData()
  },
  methods: {
    // 处理当前选中节点改变方法
    handleCurrentChange(data, node) {
      if (this.treeSelectedNode !== data) {
        console.log('handleCurrentChange:重新赋值', data, node)
        this.treeSelectedNode = data
      }
      if (node.expanded === false) {
        node.expanded = true
      }
    },

    // 获得栏目树数据
    getTreeData() {
      getAllColumns().then(res => {
        const columns = res
        this.siteNodeOnTree.children = columns
        this.treeData = [this.siteNodeOnTree]
        if (this.treeSelectedNode === null) {
          this.treeSelectedNode = this.siteNodeOnTree
        }
        this.$nextTick(() => {
          this.$refs.columnTree.setCurrentKey(this.treeSelectedNode.termTaxonomyId)
          const theNode = this.$refs.columnTree.getNode(this.treeSelectedNode.termTaxonomyId)
          this.handleCurrentChange(theNode.data, theNode)
          console.log('获得当前选中节点：：：', theNode)
          console.log('当前tableKey', this.tableKey)
          console.log('当前treeSelectedNode', this.treeSelectedNode, theNode.data)
          loopExpendTree(this.$refs.columnTree, theNode, -1)
          if (this.searchTreeText !== '') {
            this.searchTree()
          }
        })
      })
    },
    // 触发搜索树方法
    searchTree() {
      this.$refs.columnTree.filter(this.searchTreeText)
    },
    // 按名称搜索树节点方法
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    // 处理table选中行改变方法
    handleSelectionChange(val) {
      console.log('table选中行改变', val)
    },
    // 重置栏目编辑对象
    resetSiteDataModel(currentColumnId) {
      this.columnDataModel = {
        termTaxonomyId: undefined,
        name: '',
        attribute: '',
        description: '',
        parentId: currentColumnId
        // siteId: this.currentSiteId
      }
    },
    handleCreate() {
      this.resetSiteDataModel(this.$refs.columnTree.getCurrentKey())
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          // this.temp.author = 'vue-element-admin'
          this.dialogLoading = true
          const postData = qs.stringify(this.columnDataModel)
          addColumn(postData).then(() => {
            this.dialogFormVisible = false
            this.getTreeData()
            this.resetSiteDataModel()
            this.dialogLoading = false
            this.$notify({
              title: '成功',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      const columnId = row.termTaxonomyId
      getOneColumn({ termTaxonomyId: columnId }).then(res => {
        this.columnDataModel = res
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.dialogLoading = true
          const postData = qs.stringify(this.columnDataModel)
          updateColumn(postData).then(() => {
            const index = this.treeSelectedNode.children.findIndex(v => v.termTaxonomyId === this.columnDataModel.termTaxonomyId)
            this.treeSelectedNode.children.splice(index, 1, this.columnDataModel)
            this.dialogFormVisible = false
            this.dialogLoading = false
            this.$notify({
              title: '成功',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      const confirmMes = '是否确认删除该栏目及其下属栏目和所有要删除栏目当中的所有文章？'
      this.$confirm(confirmMes, '系统提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteColumn(row.termTaxonomyId).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.treeSelectedNode.children.splice(index, 1)
        })
      })
    }
  }
}
</script>
<style scoped>
.left-part {
  width: 35%;
  padding-right: 10px;
}
.right-part {
  width: 65%;
  padding-left: 10px;
}
.tree-area {
  margin-top: 10px;
}

.table-container {
  height: calc(100% - 41px);
  box-sizing: border-box;
  padding-top: 8px;
}
</style>
