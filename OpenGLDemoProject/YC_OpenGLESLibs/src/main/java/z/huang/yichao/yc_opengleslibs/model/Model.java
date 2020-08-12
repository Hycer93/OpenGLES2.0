package z.huang.yichao.yc_opengleslibs.model;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

import z.huang.yichao.yc_opengleslibs.render.Program;

public class Model {
    private Program program;
    private MtlModel mtlModel;
    private final float[] modelMatrix = new float[16];//模型矩阵
    private ModelNode[] nodes;
    private int size;
    private final float[] DEFAULT_COLOR = {0f, 0f, 1f, 0.5f};//默认颜色

    public Model(int size) {
        this.size = size;
        nodes = new ModelNode[size];
    }

    public void addNode(String name, int type, int parent, FloatBuffer vertexBuffer,
                        FloatBuffer textureBuffer, FloatBuffer normalBuffer,int faceVertexCount){
        ModelNode node = new ModelNode(name, type, parent, vertexBuffer, textureBuffer,
                normalBuffer, faceVertexCount);
        for (int i = 0; i < size; i++) {
            if (null == nodes[i]) {
                nodes[i] = node;
                return;
            }
        }
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public void setMtlModel(MtlModel mtlModel) {
        this.mtlModel = mtlModel;
    }

    public void draw(){
        GLES20.glUseProgram(program.getProgram());
        for (int i = 0; i < nodes.length; i++) {
            ModelNode modelNode = nodes[i];
            if (modelNode != null) {
                modelNode.draw();
            }
        }
    }

    private class ModelNode {
        public static final int TYPE_ROOT = 0;
        public static final int TYPE_OBJ = 1;
        public static final int TYPE_GROUP = 2;
        public static final int TYPE_MTL = 3;
        public static final int TYPE_UNIT = 4;

        private FloatBuffer vertexBuffer;
        private FloatBuffer textureBuffer;
        private FloatBuffer normalBuffer;

        private String info;
        private int type;
        private int parent;
        private int faceVertexCount;

        public ModelNode(String info, int type, int parent, FloatBuffer vertexBuffer,
                         FloatBuffer textureBuffer, FloatBuffer normalBuffer, int faceVertexCount) {
            this.vertexBuffer = vertexBuffer;
            this.textureBuffer = textureBuffer;
            this.normalBuffer = normalBuffer;
            this.info = info;
            this.type = type;
            this.parent = parent;
            this.faceVertexCount = faceVertexCount;
        }

        public void draw(){
            if (type == TYPE_MTL){
                if (info != null) {
                    MtlModel.MtlUnit unit =  mtlModel.mtlUnits.get(info);
                    if (unit != null) {
                        unit.initTextureData(program);
                        unit.initLightingDefinition(program);
                    }
                }
            } else if (type == TYPE_UNIT){
                vertexBuffer.position(0);
                GLES20.glVertexAttribPointer(program.aPosition, 3,
                        GLES20.GL_FLOAT, false, 12, vertexBuffer);
                GLES20.glEnableVertexAttribArray(program.aPosition);

                normalBuffer.position(0);
                GLES20.glVertexAttribPointer(program.aNormal, 3,
                        GLES20.GL_FLOAT, false, 12, normalBuffer);
                GLES20.glEnableVertexAttribArray(program.aNormal);

                textureBuffer.position(0);
                GLES20.glVertexAttribPointer(program.aTextureCoordinates, 2,
                        GLES20.GL_FLOAT, false, 8, textureBuffer);
                GLES20.glEnableVertexAttribArray(program.aTextureCoordinates);

                if (faceVertexCount == 3) {
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexBuffer.limit() / 3);
                } else {
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexBuffer.limit() / 4);
                }

//                GLES20.glDisableVertexAttribArray(program.aPosition);
//                GLES20.glDisableVertexAttribArray(program.aTextureCoordinates);
//                GLES20.glDisableVertexAttribArray(program.aNormal);
            }
        }
    }
}
