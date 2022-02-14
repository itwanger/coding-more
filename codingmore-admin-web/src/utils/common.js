/**
 * 创建uuid方法
 */
export const createUuid = () => {
  var s = []
  var hexDigits = '0123456789abcdef'
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = '4'
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)
  s[8] = s[13] = s[18] = s[23] = '-'

  var uuid = s.join('')
  return uuid
}

// 逐层展开当前节点
export function loopExpendTree(treeObject, currentNode, rootId) {
  currentNode.expanded = true
  while (currentNode.data.parentId !== rootId) {
    currentNode = treeObject.getNode(currentNode.data.parentId)
    if (!currentNode.expanded) {
      currentNode.expanded = true
    }
  }
}

// 合并相同对象相同key值的方法
export function assignSameProperty(targetObject, otherObject) {
  if (targetObject && otherObject) {
    let tempArr = []
    Object.keys(otherObject).forEach(key => {
      if (!targetObject.hasOwnProperty(key)) {
        tempArr.push(key)
      }
    })
    tempArr.forEach(key => {
      Reflect.deleteProperty(otherObject, key)
    })
    return Object.assign(targetObject, otherObject)
  } else {
    return targetObject || otherObject
  }
}
