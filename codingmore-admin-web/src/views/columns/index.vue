<template>
  <div v-loading="viewLoading" class="app-container">
    <el-row type="flex" :gutter="20">
      <!--左侧部分-->
      <el-col :span="6">
        <div class="filter-container">
          <div>
            <el-input v-model="searchTreeText" :placeholder="$t('columns.columnName')" @keyup.enter.native="searchTree">
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
              {{ $t('operation.add') }}
            </el-button>
            <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleCreate">
              {{ $t('operation.delete') }}
            </el-button>
          </div>
        </div>
        <el-table ref="multipleTable" :key="tableKey" :data="treeSelectedNode !== null ? treeSelectedNode.children : []" border fit highlight-current-row style="width: 100%;" @selection-change="handleSelectionChange">
          <el-table-column align="center" class-name="recorrect-center" type="selection" width="55px" />
          <el-table-column :label="$t('columns.id')" prop="termTaxonomyId" align="center" width="80px">
            <!-- <template slot-scope="{row}">
              <span>{{ row.termTaxonomyId }}</span>
            </template> -->
          </el-table-column>
          <el-table-column :label="$t('columns.columnName')" prop="name" width="200px" />
          <el-table-column :label="$t('columns.columnOutlineTemplate')" prop="tplPath" width="200px" />
          <el-table-column :label="$t('columns.columnDetailTemplate')" prop="contentTplPath" width="200px" />
          <el-table-column :label="$t('columns.columnDesc')" prop="description" />
          <el-table-column :label="$t('operation.actions')" align="center" width="230px" class-name="small-padding fixed-width">
            <template slot-scope="{row,$index}">
              <el-button type="primary" size="mini" @click="handleUpdate(row)">
                {{ $t('operation.edit') }}
              </el-button>
              <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
                {{ $t('operation.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-dialog v-loading="dialogLoading" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="columnDataModel" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <el-form-item :label="$t('columns.columnName')" prop="name">
          <el-input v-model="columnDataModel.name" maxlength="20" />
        </el-form-item>
        <el-form-item :label="$t('columns.columnOutlineTemplate')" prop="tplPath">
          <el-select v-model="columnDataModel.tplPath" class="filter-item" :placeholder="$t('operation.pleaseSelect')">
            <el-option v-for="item in avaliableOutlineTemplateList" :key="item.key" :label="item.label" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('columns.columnDetailTemplate')" prop="contentTplPath">
          <el-select v-model="columnDataModel.contentTplPath" class="filter-item" :placeholder="$t('operation.pleaseSelect')">
            <el-option v-for="item in avaliableDetileTemplateList" :key="item.key" :label="item.label" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('columns.columnDesc')">
          <el-input v-model="columnDataModel.description" :autosize="{ minRows: 4, maxRows: 6}" type="textarea" placeholder="Please input" maxlength="1000" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('operation.cancel') }}
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          {{ $t('operation.save') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllColumns, addColumn, updateColumn, deleteColumn, getOneColumn, getOutLineTemplateList, getDetailTemplateList } from '@/api/column'
import waves from '@/directive/waves' // waves directive
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
  directives: { waves },
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

      viewLoading: true,
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
        tplPath: '',
        contentTplPath: '',
        description: '',
        parentId: -1,
        siteId: this.currentSiteId
      },

      // 是否显示编辑弹窗
      dialogFormVisible: false,
      // 编辑弹窗当前状态
      dialogStatus: '',
      // 编辑弹窗显示标题字典
      textMap: {
        update: this.$t('columns.editColumn'),
        create: this.$t('columns.createColumn')
      },
      // 编辑弹窗校验规则
      rules: {
        name: [{ required: true, validator: emptyChecker, message: this.$t('columns.validNotEmptyColumnName'), trigger: 'blur' }],
        tplPath: [{ required: true, validator: emptyChecker, message: this.$t('columns.validNotEmptyOutlineTemplate'), trigger: 'blur' }],
        contentTplPath: [{ required: true, validator: emptyChecker, message: this.$t('columns.validNotEmptyDetailTemplate'), trigger: 'blur' }]
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
    currentSiteId() {
      return this.$store.getters.siteManagement.currentSiteId
    },
    currentSite() {
      return this.$store.getters.siteManagement.currentSite
    }
  },
  mounted() {
    // 初始化站点节点
    this.siteNodeOnTree = {
      termTaxonomyId: 0,
      name: this.currentSite.siteName,
      tplPath: this.currentSite.telName,
      tplDetailPath: '',
      description: '',
      parentId: -1,
      siteId: this.currentSiteId,
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

    // 获得可选择的概览模板
    updateOutlineTemplateList() {
      getOutLineTemplateList({ siteTemplate: this.currentSite.telName }).then((response) => {
        this.avaliableOutlineTemplateList = response.result.map(item => {
          return { key: item, label: item }
        })
      })
    },

    // 获得可选择的细览模板
    updateDetailTemplateList() {
      getDetailTemplateList({ siteTemplate: this.currentSite.telName }).then((response) => {
        this.avaliableDetileTemplateList = response.result.map(item => {
          return { key: item, label: item }
        })
      })
    },

    // 获得栏目树数据
    getTreeData() {
      this.viewLoading = true
      getAllColumns({ siteId: this.currentSiteId }).then(response => {
        const columns = response.result
        this.siteNodeOnTree.children = columns
        this.treeData = [this.siteNodeOnTree]
        if (this.treeSelectedNode === null) {
          this.treeSelectedNode = this.siteNodeOnTree
        }
        this.viewLoading = false
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
        tplPath: '',
        tplDetailPath: '',
        description: '',
        parentId: currentColumnId,
        siteId: this.currentSiteId
      }
    },
    // 获得站点树节点——即根节点
    getTreeNodeOfSite() {
      return {
        termTaxonomyId: 0,
        name: this.currentSite.siteName,
        tplPath: '',
        tplDetailPath: '',
        description: '',
        parentId: -1,
        siteId: this.currentSiteId
      }
    },
    handleCreate() {
      this.updateOutlineTemplateList()
      this.updateDetailTemplateList()
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
              title: this.$t('operation.success'),
              message: this.$t('operation.operationSuccessed'),
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.updateOutlineTemplateList()
      this.updateDetailTemplateList()
      const columnId = row.termTaxonomyId
      this.viewLoading = true
      getOneColumn({ termTaxonomyId: columnId }).then(response => {
        this.columnDataModel = response.result
        this.dialogStatus = 'update'
        this.viewLoading = false
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
              title: this.$t('operation.success'),
              message: this.$t('operation.operationSuccessed'),
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      const that = this
      const confirmMes = that.$t('columns.deleteWarning')
      this.$confirm(confirmMes, that.$t('operation.systemInfo'), {
        confirmButtonText: that.$t('operation.confirm'),
        cancelButtonText: that.$t('operation.cancel'),
        type: 'warning'
      }).then(() => {
        this.viewLoading = true
        deleteColumn(row.termTaxonomyId).then(() => {
          this.$notify({
            title: this.$t('operation.success'),
            message: this.$t('operation.deleteSuccessed'),
            type: 'success',
            duration: 2000
          })
          this.treeSelectedNode.children.splice(index, 1)
          this.viewLoading = false
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
</style>
