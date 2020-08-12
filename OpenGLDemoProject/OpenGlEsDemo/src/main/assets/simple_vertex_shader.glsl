uniform mat4 u_Matrix;//MVP矩阵
attribute vec4 a_Position;//顶点位置

void main()
{
    gl_Position = u_Matrix * a_Position;
}