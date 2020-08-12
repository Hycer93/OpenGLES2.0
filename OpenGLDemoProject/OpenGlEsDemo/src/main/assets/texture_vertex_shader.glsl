attribute vec2 a_TextureCoordinates;
attribute vec3 a_Normal;
attribute vec3 a_Position;
uniform mat4 u_Matrix;
uniform vec3 u_Light;//光源位置
uniform vec3 u_Camera;//摄像机位置
uniform vec3 u_Ka;
uniform vec3 u_Kd;
uniform vec3 u_Ks;
uniform float u_Ns;
varying vec2 v_TextureCoordinates;
varying vec4 v_Diffuse;//散射光最终强度
varying vec4 v_Ambient;//环境光最终强度
varying vec4 v_Specular;//镜面光最终强度

void main() {
    vec3 targetNormal = a_Normal + a_Position;
    vec3 newNormal = (u_Matrix * vec4(targetNormal,1)).xyz - (u_Matrix * vec4(a_Position,1)).xyz;
    newNormal = normalize(newNormal);//对法向量规格化
    vec3 eye = normalize(u_Camera - (u_Matrix * vec4(a_Position,1)).xyz);//顶点到camera的向量
    vec3 vp = normalize(u_Light - (u_Matrix * vec4(a_Position,1)).xyz);//顶点到光源的向量
    vec3 halfVector = normalize(vp + eye);
    float nDotViewPosition = max(0.0,dot(newNormal,vp));//求法向量与vp的点积与0的最大值
    v_Diffuse = vec4(u_Kd,1.0) * nDotViewPosition;//计算散射光的最终强度
    float nDotViewHalfVector = dot(newNormal,halfVector);//法线与半向量的点积
    float powerFactor = max(0.0,pow(nDotViewHalfVector,u_Ns));//镜面反射光强度因子
    v_Specular = vec4(u_Ks,1.0) * powerFactor;
    v_Ambient = vec4(u_Ka,1.0);
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * vec4(a_Position,1);
}
