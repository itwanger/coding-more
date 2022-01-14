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
