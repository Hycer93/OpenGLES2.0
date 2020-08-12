package z.huang.yichao.yc_opengleslibs.model;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

import java.nio.FloatBuffer;
import java.util.HashMap;

import z.huang.yichao.yc_opengleslibs.render.Program;

public class MtlModel {
    private static final String TAG = "MtlModel";
    public String name;
    public HashMap<String,MtlUnit> mtlUnits;
    private HashMap<String, Integer> textureIds;

    public MtlModel() {
        mtlUnits = new HashMap<>();
    }

    public MtlUnit addMtlUnit(String mtl){
        MtlUnit unit = new MtlModel.MtlUnit(mtl);
        mtlUnits.put(mtl,unit);
        return unit;
    }

    public HashMap<String,MtlUnit> getMtlUnits() {
        return mtlUnits;
    }

    public void setTextureIds(HashMap<String, Integer> textureIds) {
        this.textureIds = textureIds;
    }

    public class MtlUnit {
        public final String name;
        public FloatBuffer ka_Color;
        public FloatBuffer kd_Color;
        public FloatBuffer ks_Color;
        public int illum;
        public float ns = 0;
        public float alpha = 1;
        public String ka_Texture;
        public String kd_Texture;
        public String ks_ColorTexture;
        public String ns_Texture;
        public String alphaTexture;
        public String bumpTexture;

        public MtlUnit(String name) {
            this.name = name;
        }

        public void initLightingDefinition(Program program) {
            GLES20.glUniform3fv(program.uKa,1,ka_Color);
            GLES20.glUniform3fv(program.uKd,1,kd_Color);
            GLES20.glUniform3fv(program.uKs,1,ks_Color);
            GLES20.glUniform1f(program.uNs,ns);
            GLES20.glUniform1f(program.uAlpha,alpha);
        }

        public void initTextureData(Program program) {
            if (kd_Texture != null) {
                forAllTextureData(program, kd_Texture);
            }
        }

        private void forAllTextureData(Program program,String str){
            Object value = textureIds.get(str);
            int textureId = value == null ? 0 : (int) value;
            int offset = textureId - 1;
            if (offset >= 0) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + offset);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
                GLES20.glUniform1i(program.uTextureUnit, offset);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            }
        }

        @NonNull
        @Override
        public String toString() {
            return "\nMtlUnit: name["+name+"]\nka_Color["+ka_Color+"]\nkd_Color["+kd_Color+
                    "]\nks_Color["+ks_Color+"]\nillum["+illum+"]\nns["+ns+"]\nalpha["+alpha+
                    "]\nka_Texture["+ka_Texture+"]\nkd_Texture["+kd_Texture+
                    "]\nks_ColorTexture["+ks_ColorTexture+"]\nns_Texture["+ns_Texture+
                    "]\nalphaTexture["+alphaTexture+"]\nbumpTexture["+bumpTexture+"]";
        }
    }
}
