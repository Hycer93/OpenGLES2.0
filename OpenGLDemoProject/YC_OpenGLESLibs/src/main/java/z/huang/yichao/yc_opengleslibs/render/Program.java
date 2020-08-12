package z.huang.yichao.yc_opengleslibs.render;

import android.opengl.GLES20;

import z.huang.yichao.yc_opengleslibs.utils.ShaderHelper;

public class Program {
    private static final String TAG = "Program";

    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final String A_NORMAL = "a_Normal";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_LIGHT = "u_Light";
    private static final String U_CAMERA = "u_Camera";
    private static final String U_KA = "u_Ka";
    private static final String U_KD = "u_Kd";
    private static final String U_KS = "u_Ks";
    private static final String U_NS = "u_Ns";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private static final String U_ALPHA = "u_Alpha";
    private static final String U_COLOR = "u_Color";

    public int aTextureCoordinates;
    public int aNormal;
    public int aPosition;
    public int uMatrix;
    public int uLight;
    public int uCamera;
    public int uKa;
    public int uKd;
    public int uKs;
    public int uNs;
    public int uTextureUnit;

    public int uAlpha;
    public int uColor;

    private int program;
    private String name;

    public Program(String name,String vertexCode,String fragmentCode){
        this.name = name;
        program = ShaderHelper.linkProgram(
                ShaderHelper.compileVertexShader(vertexCode),
                ShaderHelper.compileFragmentShader(fragmentCode));
        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aNormal = GLES20.glGetAttribLocation(program, A_NORMAL);
        aPosition = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrix = GLES20.glGetUniformLocation(program, U_MATRIX);
        uLight = GLES20.glGetUniformLocation(program, U_LIGHT);
        uCamera = GLES20.glGetUniformLocation(program, U_CAMERA);
        uKa = GLES20.glGetUniformLocation(program, U_KA);
        uKd = GLES20.glGetUniformLocation(program, U_KD);
        uKs = GLES20.glGetUniformLocation(program, U_KS);
        uNs = GLES20.glGetUniformLocation(program, U_NS);
        uTextureUnit = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
        uAlpha = GLES20.glGetUniformLocation(program, U_ALPHA);
        uColor = GLES20.glGetUniformLocation(program, U_COLOR);
    }

    public int getProgram() {
        return program;
    }

    public String getName() {
        return name;
    }
}
