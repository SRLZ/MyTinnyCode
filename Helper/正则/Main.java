package main.learning2;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) {
        ArrayList<String> files = new ArrayList<>();
        String base = "C:/Users/Administrator/Desktop/trash/";
        files.add("e52141.h");
        files.add("e52141_cfg.h");
        files.add("l9305.h");
        files.add("l9305_cfg.h");
        files.add("psi5accsensor.h");
        files.add("psi5anglesensor.h");
        files.add("relaydriver.h");
        String raw = readFile(base + files.get(2));
        //System.out.println(raw);
//        raw= raw.replaceAll(" \\* @brief ","\t\t  ");
//        raw=raw.replaceAll(" \\* @param[in] ","\t\t  ");
//        raw=raw.replaceAll(" \\* @retval ","\t\t  ");
//        raw=raw.replaceAll(" \\* @brief ","\t\t  ");
//        raw=raw.replaceAll(" \\* @param[out] ","");
//        System.out.println(raw);
        helpForOut(raw,"hello");

    }

    static void helpForOut(String raw,String name) {

        /**
         * @ingroup
         * @brief E52141ReadAccSensorInformation
         * @param[out] pSensorStatus:pointer to 4 PSI5AccStatusType array to store the channel is init OK
         * @retval E52141_OK: OK
         * @retval E52141_RESPONSE_XCRC_ERR: XCRC error
         * @retval E52141_RESPONSE_BLOCK_NO_DATA: Block is empty
         */
        // void E52141ReadAccSensorInformation(PSI5AccStatusType *pSensorStatus);
        Pattern pattern = Pattern.compile("(/\\*\\*\n)((\\s+\\*\\s+)([^\\n\\r]*)(\n))+(\\s+\\*/\n)([^\\n\\r]*;)");
        Matcher matcher = pattern.matcher(raw);
        while (matcher.find()) {
            String s = matcher.group(0);
            //System.out.println(s);
            Pattern patternret = Pattern.compile("(\\s+\\*/\n)([0-9a-zA-Z]*)(\\s+[0-9a-zA-Z]*)(\\([^\\n\\r]*\\))(;)");
            Matcher matcher4 = patternret.matcher(s);
            String ret="";
            String func="";
            String retValue="";
            String brief="";
            String in="";
            String retVal="";
            while (matcher4.find()) {
                ret = matcher4.group(4);
                ret=ret.replaceAll("\\(","");
                ret=ret.replaceAll("\\)","");
                func=matcher4.group(3);
                retValue=matcher4.group(2);
                //System.out.println("ret " + ret);
                //System.out.println("func "+func);
                //System.out.println("retValue "+retValue);

            }
            ret=ret.replaceAll(","," ");
            ret=ret.replaceAll("const","");
            ret=ret.replaceAll(" \\*","* ");
            //System.out.println("参数： "+ret);
            String[]type=ret.trim().split("\\s+");
//            for (String ele:
//                 type) {
//                System.out.println("type: "+ele);
//            }

            System.out.printf("程序名称：%s\n",func);
            Pattern patternBrief = Pattern.compile("(\\s+\\*\\s+)(@brief)([^\\n\\r]*)(\n)");
            Matcher matcher1 = patternBrief.matcher(s);
            while (matcher1.find()) {
                brief = matcher1.group(3);
                System.out.println("描述： " + brief);
            }

            System.out.println("输入参数：");
            if(type.length!=1) {
                int index = 0;
                int tick = 1;
                while (index < type.length) {
                    System.out.printf("%d )\t%s: 数据类型为%s,参数取值为：- \n", tick, type[index + 1], type[index]);
                    index += 2;
                    tick++;
                }
            }
            if("void".equals(retValue)){
                System.out.println("输出参数为： None");
            }else{
                System.out.println("输出参数:");
                System.out.printf("%d )\t%s: 数据类型为%s,参数取值为： \n",1,retValue,"枚举类型");
                Pattern patternretVal = Pattern.compile("(\\s+\\*\\s+)(@retval)([^\\n\\r]*)(\n)");
                Matcher matcher2 = patternretVal.matcher(s);
                while (matcher2.find()) {
                    retVal = matcher2.group(3);
                    System.out.println("\t\t  " + retVal);
                }
            }


//            Pattern patternParam = Pattern.compile("(\\s+\\*\\s+)(@param)([^\\n\\r]*)(\n)");
//            Matcher matcher2 = patternParam.matcher(s);
//            int index=0;
//            while (matcher2.find()) {
//                in = matcher2.group(3);
//                in = in.replaceAll("\\[in\\]", "");
//                in = in.replaceAll("\\[out\\]", "");
//                System.out.printf("%d )\t%s: 数据类型为%s，参数取值为：- ",index,type[]);
//            }

            System.out.println("");
        }
    }

   
