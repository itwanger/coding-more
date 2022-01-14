<template>
  <el-select v-model="selectVal" size="mini">
    <el-option v-for="(item, index) in userSiteList" :key="index" :label="item.siteName" :value="item.siteId" />
  </el-select>
</template>
<script>
export default {
  name: 'SiteSelect',
  computed: {
    userSiteList() {
      return this.$store.getters.siteManagement.siteList
    },
    selectVal: {
      get() {
        return this.$store.getters.siteManagement.currentSiteId
      },
      set(val) {
        if (this.$store.getters.siteManagement.currentSiteId === val) {
          return false
        }
        this.$store.dispatch('user/changeSite', val)

        const { fullPath } = this.$route
        this.$nextTick(() => {
          this.$router.replace({
            path: '/redirect' + fullPath
          })
        })
      }
    }
  }
}
</script>
