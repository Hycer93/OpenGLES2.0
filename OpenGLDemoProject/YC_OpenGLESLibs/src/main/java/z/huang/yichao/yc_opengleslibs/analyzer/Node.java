package z.huang.yichao.yc_opengleslibs.analyzer;

import java.util.ArrayList;

public class Node {
    private String info;
    private int parent;
    private int type;
    public static final int TYPE_ROOT = 0;
    public static final int TYPE_OBJ = 1;
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_MTL = 3;
    public static final int TYPE_UNIT = 4;
    public static final String[] TYPE = new String[]{"root","obj","group","mtl","unit"};
    private ArrayList<Float> vertexArray;
    private ArrayList<Float> textureArray;
    private ArrayList<Float> normalArray;
    private int faceVertexCount = 3;

    public Node(int parent, int type){
        this.parent = parent;
        this.type = type;
    }

    public int getParent() {
        return parent;
    }

    public int getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFaceVertexCount() {
        return faceVertexCount;
    }

    public void setFaceVertexCount(int faceVertexCount) {
        this.faceVertexCount = faceVertexCount;
    }

    public void addVertex(float vertex) {
        if (vertexArray == null){
            vertexArray = new ArrayList<>();
        }
        vertexArray.add(vertex);
    }

    public void addTexture(float texture) {
        if (textureArray == null){
            textureArray = new ArrayList<>();
        }
        textureArray.add(texture);
    }

    public void addNormal(float normal) {
        if (normalArray == null){
            normalArray = new ArrayList<>();
        }
        normalArray.add(normal);
    }

    public ArrayList<Float> getVertexArray() {
        return vertexArray;
    }

    public ArrayList<Float> getTextureArray() {
        return textureArray;
    }

    public ArrayList<Float> getNormalArray() {
        return normalArray;
    }
}
