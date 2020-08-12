package z.huang.yichao.yc_opengleslibs.analyzer;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import z.huang.yichao.yc_opengleslibs.define.Constants;
import z.huang.yichao.yc_opengleslibs.model.Model;


public class ModelAnalyzer {
    private static final String TAG = ModelAnalyzer.class.getSimpleName();
    /**
     * obj需解析字段
     */
    // obj对应的材质库
    private static final String MTLLIB = "mtllib";
    // 组名称
    private static final String G = "g";
    // 对象名称(Object name)
    private static final String O = "o";
    // 顶点
    private static final String V = "v";
    // 纹理坐标
    private static final String VT = "vt";
    // 顶点法线
    private static final String VN = "vn";
    // 使用的材质
    private static final String USEMTL = "usemtl";
    // 索引:起始于1
    private static final String F = "f";

    private ArrayList<Float> vertexData;
    private ArrayList<Float> textureData;
    private ArrayList<Float> normalData;
    private ArrayList<String> mtlArray;

    private ModelTree modelTree;
    private Node curNode;
    private Node leaveNode;

    public boolean analyzeObj(BufferedReader assetsBuffer) {
        modelTree = new ModelTree();
        curNode = new Node(-1, Node.TYPE_ROOT);//创建根节点
        curNode.setInfo("parent");
        modelTree.addNode(curNode);
        try {
            String line;
            while ((line = assetsBuffer.readLine()) != null) {
                sortData(line.split("\\s"));
            }
            assetsBuffer.close();
            scanOver();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //解析Obj数据
    private void sortData(String[] data) {
        switch (data[0]) {
            case MTLLIB:
                loadMtlLib(data);
                break;
            case O:
                leaveNode = null;
                Node objNode = addNode(curNode,Node.TYPE_OBJ);
                objNode.setInfo(data[1]);
                curNode = objNode;
                break;
            case G:
                leaveNode = null;
                Node groupNode = addNode(curNode,Node.TYPE_GROUP);
                groupNode.setInfo(data[1]);
                curNode = groupNode;
                break;
            case USEMTL:
                leaveNode = null;
                Node mtlNode = addNode(curNode,Node.TYPE_MTL);
                mtlNode.setInfo(data[1]);
                curNode = mtlNode;
                break;
            case V:
                loadVertex(data);
                break;
            case VT:
                loadTexture(data);
                break;
            case VN:
                loadNormal(data);
                break;
            case F:
                loadFace(data);
                break;
        }
    }

    private Node addNode(Node curNode,int nodeType) {
        Node parent = null;
        //如果当前记录的节与即将添加的节点点的类型一致说明同级节点，添加到当前结点的父节点中。
        if (curNode.getType() == nodeType) {
            parent = modelTree.getParent(curNode);
        } else {//如果类型不一致，则需要判断父节点有没有同级节点，添加至同级节点的父节点,否则添加为子节点
            boolean flag = true;
            Node tmp = curNode;
            while (flag) {
                tmp = modelTree.getParent(tmp);
                if (tmp == null){
                    flag = false;
                } else {
                    if (tmp.getType() == nodeType) {
                        parent = modelTree.getParent(tmp);
                        flag = false;
                    }
                }
            }
        }
        if (parent == null){
            parent = curNode;
        }
        Node newNode = new Node(modelTree.position(parent), nodeType);
        modelTree.addNode(newNode);
        return newNode;
    }

    private void loadMtlLib(String[] mtl) {
        if (mtlArray == null) {
            mtlArray = new ArrayList<>();
        }
        for (int i = 1; i < mtl.length; i++) {
            if (!mtl[i].equals("")) {
                mtlArray.add(mtl[i]);
            }
        }
    }

    //读取顶点坐标数据
    private void loadVertex(String[] vertex) {
        if (vertexData == null) {
            vertexData = new ArrayList<>();
        }
        vertexData.add(Float.parseFloat(vertex[1]));
        vertexData.add(Float.parseFloat(vertex[2]));
        vertexData.add(Float.parseFloat(vertex[3]));
    }

    //读取纹理坐标数据
    private void loadTexture(String[] texture) {
        if (textureData == null) {
            textureData = new ArrayList<>();
        }
        textureData.add(Float.parseFloat(texture[1]));
        textureData.add(Float.parseFloat(texture[2]));
    }

    //读取法线坐标数据
    private void loadNormal(String[] normal) {
        if (normalData == null) {
            normalData = new ArrayList<>();
        }
        normalData.add(Float.parseFloat(normal[1]));
        normalData.add(Float.parseFloat(normal[2]));
        normalData.add(Float.parseFloat(normal[3]));
    }

    //读取面数据
    private void loadFace(String[] faceData) {
        if (leaveNode == null) {
            leaveNode = new Node(modelTree.position(curNode), Node.TYPE_UNIT);
            leaveNode.setInfo("data");
            leaveNode.setFaceVertexCount(faceData.length - 1);
            modelTree.addNode(leaveNode);
        }
        for (int i = 1; i < faceData.length; i++) {
            if (!faceData[i].equals("")) {
                String[] facePointData = faceData[i].split("/");
                //vertex point
                int vertexIndex = (Integer.parseInt(facePointData[0]) - 1) * 3;
                leaveNode.addVertex(vertexData.get(vertexIndex));
                leaveNode.addVertex(vertexData.get(vertexIndex + 1));
                leaveNode.addVertex(vertexData.get(vertexIndex + 2));
                //texture point
                int textureIndex = (Integer.parseInt(facePointData[1]) - 1) * 2;
                leaveNode.addTexture(textureData.get(textureIndex));
                leaveNode.addTexture(textureData.get(textureIndex + 1));
                //normal point
                int normalIndex = (Integer.parseInt(facePointData[2]) - 1) * 3;
                leaveNode.addNormal(normalData.get(normalIndex));
                leaveNode.addNormal(normalData.get(normalIndex + 1));
                leaveNode.addNormal(normalData.get(normalIndex + 2));
            }
        }
    }

    //Obj文件扫描完成
    private void scanOver() {
        vertexData.clear();
        textureData.clear();
        normalData.clear();
        vertexData = null;
        textureData = null;
        normalData = null;
        curNode = null;
        leaveNode = null;
    }

    private FloatBuffer buildBuffer(ArrayList<Float> bufferList) {
        if (bufferList != null) {
            float[] buffer = ArrayUtils.toPrimitive(bufferList.toArray(new Float[0]));
            FloatBuffer fb = ByteBuffer.allocateDirect(buffer.length * Constants.BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(buffer);
            fb.position(0);
            return fb;
        }
        return null;
    }

    public Model getModel(){
        Model model = new Model(modelTree.getCount());
        Node[] children = modelTree.getAllNode();
        for (Node n : children){
            if (n != null) {
                model.addNode(
                        n.getInfo(),
                        n.getType(),
                        n.getParent(),
                        buildBuffer(n.getVertexArray()),
                        buildBuffer(n.getTextureArray()),
                        buildBuffer(n.getNormalArray()),
                        n.getFaceVertexCount()
                );
            }
        }
        return model;
    }

    public void release(){
        modelTree = null;
    }
}
