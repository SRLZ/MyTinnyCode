
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class helperUtils {
        private static LinkedList<String>myStk=new LinkedList<>();

        public static void main(String args[]) {
            try {
                List<Map<String, String>> maps = readExcel("D:\\dbc.xlsx");
                helperForExel2Dbc(maps);
            }catch (Exception e){
                System.out.println(e);
            }

        }
        //用来将文件中\t替换成四个空格
        public static void helperForRepalceToBlank() throws IOException {
            File baseFile = new File("D:\\old");
            File[]files=baseFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file=files[i];
                String des="D:\\new\\"+file.getName();
                BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(des),"GB2312"));
                String res="";
                while((res=br.readLine())!=null){
                    res=res.replaceAll("\t","    ");
                    //System.out.println(res);
                    bw.write(res);
                    bw.write("\n");
                }
                bw.flush();
                bw.close();
                br.close();
            }
        }
        //用来快速实现手册中芯片寄存器转换成联合体
        public static void readFile() {
            String pathname = "D:/input.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件

            try (FileReader reader = new FileReader(pathname);
                 BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
            ) {
                String line;
                int times=0;
                //网友推荐更加简洁的写法
                while ((line = br.readLine()) != null) {
                    // 一次读入一行数据

                    String[]temp=line.split(" ");
                    int length=temp.length;
                    StringBuilder sb=new StringBuilder();
                    sb.append("uint16").append(" ");
                    boolean flag=true;
                    for(int i=0;i<length;i++){
                        if(isNum(temp[i])) {
                            flag=false;
                            break;
                        }
                        sb.append(temp[i]).append("_");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    String digit="";
                    if(flag){
                        times++;
                        digit="1";
                    }
                    else{
                        times+=Integer.parseInt(temp[length-1]);
                        digit=temp[length-1];
                    }
                    sb.append(":").append(flag?"1":temp[length-1]).append(";");
                    myStk.addLast(sb.toString());
                }
                if(times!=16) System.out.println("false");
                int len=myStk.size();
                for (int i=0;i<len;i++){
                    System.out.println(myStk.pollLast());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    //判断字符串是否是数字
    static boolean isNum(String s) {
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }


    //仅限于测试 写文件
    public static void writeFile() {
        try {
                File writeName = new File("output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
                writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
                try (FileWriter writer = new FileWriter(writeName);
                     BufferedWriter out = new BufferedWriter(writer)
                ) {
                    out.write("我会写入文件啦1\r\n"); // \r\n即为换行
                    out.write("我会写入文件啦2\r\n"); // \r\n即为换行
                    out.flush(); // 把缓存区内容压入文件
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //自动化生成类似getter() setter()方法
        public static void helperForPowerControl() {
            String pathname = "D:/input.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件

            int times=0;
            try (FileReader reader = new FileReader(pathname);
                 BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
            ) {
                String line;

                while ((line = br.readLine()) != null) {
                    // 一次读入一行数据
                    String[]temp=line.trim().split("\\s");
                    if(temp.length<=1)break;
                    String variable=temp[1];
                    String []functionNames=variable.split("_");
                    StringBuilder fuc=new StringBuilder();
                    camelFormat(functionNames, fuc, 1);
                    times++;
                    String doc1="/**\n" +
                            " * @ingroup\n" +
                            " * @brief  get the "+variable+" control pin state\n" +
                            " * @param[in]  pCtrlState: pointer to save the control state\n" +
                            " * @return NONE\n" +
                            " */\n";
                    String doc2="/**\n" +
                            " * @ingroup\n" +
                            " * @brief  "+variable+" voltage is controlled by Dio pin\n" +
                            " * @param[in]  control: POWER_ON or POWER_OFF to control "+variable+" voltage is ON or OFF\n" +
                            " * @return NONE\n" +
                            " */\n";
                    String res1="void "+fuc.toString()+"ControlStateGet"+"(powerControlType *pCtrlState)\n" +
                            "{\n" +
                            "\t*pCtrlState = Dio_ReadChannel("+variable+");\n" +
                            "\treturn;\n" +
                            "}\n";
                    String res2="void "+fuc.toString()+"Control"+"(powerControlType control)\n" +
                            "{\n" +
                            "\tDio_WriteChannel("+variable+" , control);\n" +
                            "\treturn;\n" +
                            "}\n";
                    String res3="void "+fuc.toString()+"ControlStateGet"+"(powerControlType *pCtrlState);\n";
                    String res4="void "+fuc.toString()+"Control"+"(powerControlType control);\n";
                    //System.out.println(doc1+res1);
                    //System.out.println(doc2+res2);
                    System.out.println(doc1+res3);
                    System.out.println(doc2+res4);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(times);
        }

    //生成驼峰命名
    private static String camelFormat(String[] messageFormat, StringBuilder format,int start) {
        char[]re;
        for (int i = start; i <messageFormat.length ; i++) {
            if(i==start){
                re=messageFormat[i].toLowerCase().toCharArray();
            }else {
                re=messageFormat[i].toLowerCase().toCharArray();
                if(Character.isLetter(re[0])){
                    re[0]=Character.toUpperCase(re[0]);
                }
            }
            format.append(re);
        }
        return format.toString();
    }
    //按照指定的exel格式生成dbc文件
    public static void helperForExel2Dbc(List<Map<String, String>>exelResultByRow) throws IOException {
            if(exelResultByRow==null||exelResultByRow.size()==0)return;
            String version="VERSION \"\"\n";
            String NSymbol="NS_ : \n" +
                    "\tNS_DESC_\n" +
                    "\tCM_\n" +
                    "\tBA_DEF_\n" +
                    "\tBA_\n" +
                    "\tVAL_\n" +
                    "\tCAT_DEF_\n" +
                    "\tCAT_\n" +
                    "\tFILTER\n" +
                    "\tBA_DEF_DEF_\n" +
                    "\tEV_DATA_\n" +
                    "\tENVVAR_DATA_\n" +
                    "\tSGTYPE_\n" +
                    "\tSGTYPE_VAL_\n" +
                    "\tBA_DEF_SGTYPE_\n" +
                    "\tBA_SGTYPE_\n" +
                    "\tSIG_TYPE_REF_\n" +
                    "\tVAL_TABLE_\n" +
                    "\tSIG_GROUP_\n" +
                    "\tSIG_VALTYPE_\n" +
                    "\tSIGTYPE_VALTYPE_\n" +
                    "\tBO_TX_BU_\n" +
                    "\tBA_DEF_REL_\n" +
                    "\tBA_REL_\n" +
                    "\tBA_DEF_DEF_REL_\n" +
                    "\tBU_SG_REL_\n" +
                    "\tBU_EV_REL_\n" +
                    "\tBU_BO_REL_\n" +
                    "\tSG_MUL_VAL_\n";
            String BSBaud="BS_:\n";
            StringBuilder BUNode=new StringBuilder("New_Node1");
            StringBuilder BOMessage=new StringBuilder();
            StringBuilder SGSignal=new StringBuilder();
            ListIterator<Map<String, String>>it=exelResultByRow.listIterator();
            HashSet<String>isDefined=new HashSet<>();
            String path = "D:\\myDbc.dbc";
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path,true)));
            out.write(version);
            out.write(NSymbol);
            out.write(BSBaud);
            out.write("BU_: "+BUNode.toString()+"\n");
            int i=0;
            while (it.hasNext()){
                //BO_ 0 New_Message_1: 4 New_Node_1
                StringBuilder BO_temp=new StringBuilder("BO_ ");
                StringBuilder SG_temp=new StringBuilder("SG_ ");
                String vector_xxx="New_Node1";
                Map<String,String>ele=it.next();
                String messageId=ele.get("Message ID");
                String messageName=ele.get("Message Name");
                if(!isDefined.contains(messageName)){
                    System.out.println("================="+i+"=================");
                    BO_temp.append(Integer.parseInt(messageId.substring(2),16)).append(" ").append(ele.get("Message Name"))
                            .append(": ").append(ele.get("Message Length")).append(" ").append(BUNode).append("\n");
                    out.write(BO_temp.toString());
                    isDefined.add(messageName);
                }


                System.out.println("\tmyDbcSend."+camelFormat(ele.get("Message Name").split("_"),new StringBuilder(),0)+"Data"+
                        "."
                        +camelFormat(ele.get("Signal Name").split("_"),new StringBuilder(),0)
                        +" = pData[*];");
                SG_temp.append(ele.get("Signal Name")).append(" : ").append(ele.get("Start bit")).append("|")
                        .append(ele.get("Signal length")).append("@").append(ele.get("Byte Order").equals("intel")?1:0).append("- (")
                        .append(ele.get("Factor")).append(",").append(ele.get("Offset")).append(") ").append("[0|0] \"").append(ele.get("Unit"))
                        .append("\" ").append(vector_xxx).append("\n");
                out.write(SG_temp.toString());
                i++;

                }
            String other="BA_DEF_  \"BusType\" STRING ;\n" +
                    "BA_DEF_ BU_  \"NodeLayerModules\" STRING ;\n" +
                    "BA_DEF_ BU_  \"ECU\" STRING ;\n" +
                    "BA_DEF_ BU_  \"CANoeJitterMax\" INT 0 0;\n" +
                    "BA_DEF_ BU_  \"CANoeJitterMin\" INT 0 0;\n" +
                    "BA_DEF_ BU_  \"CANoeDrift\" INT 0 0;\n" +
                    "BA_DEF_ BU_  \"CANoeStartDelay\" INT 0 0;\n" +
                    "BA_DEF_DEF_  \"BusType\" \"\";\n" +
                    "BA_DEF_DEF_  \"NodeLayerModules\" \"\";\n" +
                    "BA_DEF_DEF_  \"ECU\" \"\";\n" +
                    "BA_DEF_DEF_  \"CANoeJitterMax\" 0;\n" +
                    "BA_DEF_DEF_  \"CANoeJitterMin\" 0;\n" +
                    "BA_DEF_DEF_  \"CANoeDrift\" 0;\n" +
                    "BA_DEF_DEF_  \"CANoeStartDelay\" 0;";
            out.write(other);
            out.flush();
            out.close();
            }
    //按行读取exel表格，每一行数据以键值对形式存储：<第一行的标题：对应的数据>
    //每一行数据都是一个HashMap
    public static List<Map<String, String>> readExcel(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()){
            throw new Exception("文件不存在!");
        }
        InputStream in = new FileInputStream(file);

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        ArrayList<Map<String, String>> list = new ArrayList<>();

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheetAt.getRow(0);
        // 循环获取每一行数据
        LinkedHashMap<String, String> mapLast = new LinkedHashMap<>();
        for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheetAt.getRow(i);
            LinkedHashMap<String, String> map = new LinkedHashMap<>(mapLast);
            // 读取每一格内容
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                XSSFCell titleCell = titleRow.getCell(index);
                XSSFCell cell = row.getCell(index);
                if(cell==null)continue;
                cell.setCellType(CellType.STRING);
                if (cell.getStringCellValue().equals("")) {
                    continue;
                }
                map.put(getString(titleCell), getString(cell));
            }
            if (map.isEmpty()) {
                continue;
            }
            list.add(map);
            mapLast=new LinkedHashMap<>(map);
        }
        return list;
    }

    //把exel单元格的内容转为字符串
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getStringCellValue();
        }
    }
}
