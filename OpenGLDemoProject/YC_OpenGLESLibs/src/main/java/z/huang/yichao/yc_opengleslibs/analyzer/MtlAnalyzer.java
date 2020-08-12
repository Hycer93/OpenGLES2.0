package z.huang.yichao.yc_opengleslibs.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import z.huang.yichao.yc_opengleslibs.define.Constants;
import z.huang.yichao.yc_opengleslibs.model.MtlModel;
import z.huang.yichao.yc_opengleslibs.utils.LogUtil;

/*-----------------------------------------*/
/*             材质结构:   		           */
/*             newmtl mymtl_1              */
/*             材质颜色光照定义	           */
/*             纹理贴图定义   	           */
/*             反射贴图定义 	               */
/*-----------------------------------------*/
public class MtlAnalyzer {
    private static final String TAG = "MtlAnalyze";
    // 定义一个名为 'xxx'的材质
    private static final String NEWMTL = "newmtl";
    /**
     * -------------------------------------材质颜色光照定义--------------------------------------
     **/
    // 材质的环境光（ambient color）
    private static final String KA = "Ka";
    // 散射光（diffuse color）
    private static final String KD = "Kd";
    // 镜面光（specular color）
    private static final String KS = "Ks";
    // 反射指数 定义了反射高光度。该值越高则高光越密集，一般取值范围在0~1000。
    private static final String NS = "Ns";
    // 滤光透射率
    private static final String TF = "Tf";
    // 指定材质的光照模型。illum后面可接0~10范围内的数字参数。
    // 光照模型 属性
    // 0 Color on and Ambient off
    // 1 Color on and Ambient on
    // 2 Highlight on
    // 3 Reflection on and Ray trace on
    // 4 Transparency: Glass on; Reflection: Ray trace on
    // 5 Reflection: Fresnel on and Ray trace on
    // 6 Transparency: Refraction on; Reflection: Fresnel off and Ray trace on
    // 7 Transparency: Refraction on; Reflection: Fresnel on and Ray trace on
    // 8 Reflection on and Ray trace off
    // 9 Transparency: Glass on; Reflection: Ray trace off
    // 10 Casts shadows onto invisible surfaces
    private static final String ILLUM = "illum";
    // 渐隐指数描述 参数factor表示物体融入背景的数量，取值范围为0.0~1.0，取值为1.0表示完全不透明，
    // 取值为0.0时表示完全透明。
    private static final String D = "d";
    // 清晰度描述 指定本地反射贴图的清晰度。材质中没有本地反射贴图定义，将此值应用到预览中的全局反射贴图。
    private static final String SHARPNESS = "Sharpness";
    // 折射值描述
    private static final String NI = "Ni";
    /**------------------------------------------------------------------------------------------**/

    /**
     * ---------------------------------------纹理贴图定义----------------------------------------
     **/
    // 环境反射指定颜色纹理文件 在渲染的时候，Ka的值将再乘上map_Ka的值。
    private static final String MAP_KA = "map_Ka";
    // 漫反射指定颜色纹理文件 作用原理与可选参数与map_Ka同
    private static final String MAP_KD = "map_Kd";
    // 镜反射指定颜色纹理文件 作用原理与可选参数与map_Ka同
    private static final String MAP_KS = "map_Ks";
    // 镜面反射指定标量纹理文件 可选参数如下所示：
    // -blendu on | off
    // -blendv on | off
    // -clamp on | off
    // -imfchan r | g | b | m | l | z
    // -mm base gain
    // -o u v w
    // -s u v w
    // -t u v w
    // -texres value
    private static final String MAP_NS = "map_Ns";
    // 消隐指数指定标量纹理文件
    private static final String MAP_D = "map_d";
    // 纹理反走样功能开关
    private static final String MAP_AAT = "map_aat";
    // 指定一个标量纹理文件或程序纹理文件用于选择性地将材质的颜色替换为纹理的颜色。可选参数同map_Ns。
    // 在渲染期间， Ka, Kd, and Ks和map_Ka, map_Kd, map_Ks的值通过下面这个公式来进行使用：
    // result_color=tex_color(tv)*decal(tv)+mtl_color * (1.0-decal(tv))
    // 其中tv表示纹理顶点，result_color是Ka,Kd和Ks的综合作用值。
    private static final String DECAL = "decal";
    // 指定一个标量纹理文件或程序纹理文件实现物体变形或产生表面粗糙。可选参数同map_Ns。
    private static final String DISP = "disp";
    // 为材质指定凹凸纹理文件 可选参数可为：
    // -bm mult
    // -clamp on | off
    // -blendu on | off
    // -blendv on | off
    // -imfchan r | g | b | m | l | z
    // -mm base gain
    // -o u v w
    // -s u v w
    // -t u v w
    // -texres value
    private static final String BUMP = "bump";
    private static final String MAP_BUMP = "map_bump";
    /**------------------------------------------------------------------------------------------**/

    /**
     * ---------------------------------------反射贴图定义----------------------------------------
     **/
    // 将指定的纹理反射映射至物体。
    private static final String REFL = "refl";
    /**
     * ------------------------------------------------------------------------------------------
     **/

    private MtlModel mtlModel;
    private MtlModel.MtlUnit curMtlUnit;
    private List<String> tgaList = new ArrayList<>();

    public MtlAnalyzer() {

    }

    public boolean analyzeMtl(String mtlName, BufferedReader assetsBuffer) {
        mtlModel = new MtlModel();
        mtlModel.name = mtlName;
        try {
            String line;
            while ((line = assetsBuffer.readLine()) != null) {
                // 忽略 空行和注释
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }
//                LogUtil.d(TAG, "analyzeMtl: "+line);
                sortData(line.split("\\s"));
            }
            scanOver();
            assetsBuffer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sortData(String[] data) {
        if (data.length == 1)return;
        if (NEWMTL.equalsIgnoreCase(data[0])) {
            curMtlUnit = mtlModel.addMtlUnit(data[1]);
        } else if (KA.equalsIgnoreCase(data[0])) {
            curMtlUnit.ka_Color = getColorFromParts(data);
        } else if (KD.equalsIgnoreCase(data[0])) {
            curMtlUnit.kd_Color = getColorFromParts(data);
        } else if (KS.equalsIgnoreCase(data[0])) {
            curMtlUnit.ks_Color = getColorFromParts(data);
        } else if (NS.equalsIgnoreCase(data[0])) {
            curMtlUnit.ns = Float.parseFloat(data[1]);
        } else if (D.equalsIgnoreCase(data[0])) {
            curMtlUnit.alpha = Float.parseFloat(data[1]);
        } else if (MAP_KA.equalsIgnoreCase(data[0])) {
            curMtlUnit.ka_Texture = data[1];
        } else if (MAP_KD.equalsIgnoreCase(data[0])) {
            curMtlUnit.kd_Texture = data[1];
        } else if (MAP_KS.equalsIgnoreCase(data[0])) {
            curMtlUnit.ks_ColorTexture = data[1];
        } else if (MAP_NS.equalsIgnoreCase(data[0])) {
            curMtlUnit.ns_Texture = data[1];
        } else if (MAP_D.equalsIgnoreCase(data[0])) {
            curMtlUnit.alphaTexture = data[1];
        } else if (MAP_BUMP.equalsIgnoreCase(data[0])) {
            curMtlUnit.bumpTexture = data[1];
        } else if (ILLUM.equalsIgnoreCase(data[0])) {
            curMtlUnit.illum = Integer.parseInt(data[1]);
        } else {
            LogUtil.d(TAG, "no parse - " + data[0]);
        }
        if (data[1].contains(".tga")){
            if (tgaList.indexOf(data[1]) == -1){
                tgaList.add(data[1]);
            }
        }
    }

    private void scanOver() {

    }

    private FloatBuffer getColorFromParts(String[] data) {
        float[] floats = new float[3];
        floats[0] = Float.parseFloat(data[1]);
        floats[1] = Float.parseFloat(data[2]);
        floats[2] = Float.parseFloat(data[3]);
        FloatBuffer fb = ByteBuffer.allocateDirect(floats.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(floats);
        fb.position(0);
        return fb;
    }

    public MtlModel getResult() {
        return mtlModel;
    }

    public List<String> getTgaList(){
        return tgaList;
    }

    public void release() {

    }

}
