<template>
  <div class="app-container config-container">
    <el-form ref="form" :model="siteInfo" :rules="rules" label-position="right" label-width="120px">
      <el-form-item label="站点名称" prop="siteName">
        <el-input v-model="siteInfo.siteName" placeholder="请填写站点信息" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="关键词" prop="keywords">
        <el-input v-model="siteInfo.keywords" placeholder="请填写关键词，不超过500字" maxlength="500" :rows="3" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="站点介绍">
        <el-input v-model="siteInfo.siteDesc" placeholder="请填写站点介绍，不超过1000字" maxlength="1000" :rows="3" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="ICP备案信息">
        <el-input v-model="siteInfo.extendInfo.icpInfo" placeholder="请填写ICP备案信息" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="公安网备案信息">
        <el-input v-model="siteInfo.extendInfo.psInfo" placeholder="请填写公安网备案信息" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="版权所有">
        <el-input v-model="siteInfo.extendInfo.copyrightInfo" placeholder="请填写版权所有信息" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="siteInfo.extendInfo.contactNumber" placeholder="请填写联系电话" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="siteInfo.extendInfo.email" placeholder="请填写邮箱" maxlength="100"></el-input>
      </el-form-item>
      <el-form-item label="通信地址">
        <el-input v-model="siteInfo.extendInfo.address" placeholder="请填写通信地址" :rows="3" maxlength="200" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <div class="text-right">
      <el-button type="primary" @click="saveData">保存</el-button>
    </div>
  </div>
</template>
<script>
import { emptyChecker } from '@/utils/validate'
import { assignSameProperty } from '@/utils/common'
import qs from 'qs'
import { getSiteConfigInfo, updateSiteConfig } from '@/api/site'
export default {
  name: 'siteConfig',
  data() {
    return {
      siteInfo: {
        siteName: '', // 站点名称
        keywords: '', // 关键字
        siteDesc: '', // 站点描述
        attribute: '', // 其他属性
        // 其他属性对象
        extendInfo: {
          icpInfo: '', // icp备案信息
          psInfo: '', // 公安网备案信息
          copyrightInfo: '', // 版权所有信息
          contactNumber: '', // 联系电话
          email: '', // 邮箱
          address: '' // 地址
        }
      },

      rules: {
        siteName: [{ required: true, validator: emptyChecker, message: '站点名称不能为空', trigger: 'blur' }],
        keywords: [{ required: true, validator: emptyChecker, message: '关键字不能为空', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 加载数据方法
    loadData() {
      getSiteConfigInfo().then(res => {
        [this.siteInfo.siteName, this.siteInfo.keywords, this.siteInfo.siteDesc] = [res.siteName, res.keywords, res.siteDesc]
        if (res.attribute) {
          this.siteInfo.extendInfo = assignSameProperty(this.siteInfo.extendInfo, res.attribute)
        }
      })
    },

    // 保存站点设置方法
    saveData() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          let { siteName, keywords, siteDesc } = this.siteInfo
          let reqData = {
            siteName, // 站点名称
            keywords, // 关键字
            siteDesc, // 站点描述
            attribute: '' // 其他属性
          }
          reqData.attribute = JSON.stringify(this.siteInfo.extendInfo)
          reqData = qs.stringify(reqData)
          updateSiteConfig(reqData).then(() => {
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    }
  },
  mounted() {
    this.loadData()
  }
}
</script>
<style scoped>
.config-container {
  width: 800px;
  margin: 0 auto;
}
</style>
