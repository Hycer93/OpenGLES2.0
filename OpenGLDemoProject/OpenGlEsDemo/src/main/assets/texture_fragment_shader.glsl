precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec4 v_Diffuse;
varying vec4 v_Ambient;
varying vec4 v_Specular;
uniform float u_Alpha;
uniform vec4 u_Color;

void main() {
    vec4 finalColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    finalColor.a *= u_Alpha;
    gl_FragColor = finalColor * v_Ambient + finalColor * v_Specular + finalColor + v_Diffuse;

//    gl_FragColor = u_Color;
}
