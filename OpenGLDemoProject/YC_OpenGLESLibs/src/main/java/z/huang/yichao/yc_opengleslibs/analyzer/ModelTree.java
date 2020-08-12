package z.huang.yichao.yc_opengleslibs.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelTree {
    private static final String TAG = "ModelTree";
    private static final int DEFAULT_SIZE = 15;
    private int size;
    private int count;
    private Node[] nodes;

    public ModelTree() {
        size = DEFAULT_SIZE;
        count = 0;
        nodes = new Node[size];
    }

    public void addNode(Node node) {
        if (count >= size) {//如果当前数量等于树预留的节点数时需要扩容
            enlarge();
        }
        for (int i = count; i < size; i++) {
            if (null == nodes[i]) {
                nodes[i] = node;
                count++;
                return;
            }
        }
    }

    private void enlarge() {
        size += DEFAULT_SIZE;
        Node[] newNodes = Arrays.copyOf(nodes,size);
        Arrays.fill(nodes,null);
        nodes = newNodes;
    }

    public Node getRoot(){
        return nodes[0];
    }

    public Node getParent(Node node){
        if (node.getParent() >= 0) {
            return nodes[node.getParent()];
        } else {
            return null;
        }
    }

    public Node[] getAllNode(){
        return nodes;
    }

    public List<Node> list(){
        return list(getRoot(),false);
    }

    public List<Node> list(boolean hasRoot){
        return list(getRoot(),hasRoot);
    }

    public List<Node> list(Node root,boolean hasRoot){
        List<Node> nodeList = new ArrayList<>();
        if (hasRoot){
            nodeList.add(root);
        }
        return list(nodeList,root);
    }

    private List<Node> list(List<Node> list,Node root){
        List<Node> children = children(root);
        if (children.size() == 0){
            return list;
        }
        for (Node n : children){
            list.add(n);
            list(list,n);
        }
        return list;
    }

    public List<Node> children(Node node) {
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (nodes[i] != null && nodes[i].getParent() == position(node)){
                list.add(nodes[i]);
            }
        }
        return list;
    }

    //获取节点在数组的存储位置
    public int position(Node node) {
        for (int i = 0; i < this.size; i++) {
            if (nodes[i] == node) {
                return i;
            }
        }
        return -1;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return children(nodes[0],0);
    }

    private String children(Node node,int level){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < level; i++) {
            s.append("\t");
        }
        s.append(node.getInfo()).append("[").append(Node.TYPE[node.getType()]).append("]");
        if (node.getType() == Node.TYPE_UNIT){
            s.append(" vertex size - "+node.getVertexArray().size() / 3);
        }
        s.append("\n");
        List<Node> children = children(node);
        if (children.size() == 0){
            return s.toString();
        } else {
            level += 1;
        }
        for (Node n : children){
            s.append(children(n,level));
        }
        return s.toString();
    }

}
