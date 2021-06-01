public class Kmp {
    public static void main(String[] args) {
        System.out.println(find("agibcdgiaabaadretbgiaabtcgaabaaeiaabaefrgf","aabaaef"));
    }

    //KMP查找
    static int find(String source,String pattern){
        int n1=source.length();
        int n2=pattern.length();
        char []par=pattern.toCharArray();
        char[]sour=source.toCharArray();
        int[]next=new int[n2];
        int k=-1;
        next[0]=-1;
        //构建next数组，其中第next[i]表示par模式串数组中最长公共前缀后缀的前缀结束的下标
        // par="aabaac" next[0]=-1 next[1]=1 next[2]=-1 next[3]=1 next[4]=1 next[5]=-1
        for(int i=1;i<n2;i++){
            while(k!=-1&&par[k+1]!=par[i])k=next[k];
            if(par[k+1]==par[i])k++;
            next[i]=k;
        }
        k=-1;
        for(int j=0;j<n1;j++){
            //如果匹配 j++,k++
            if(sour[j]==par[k+1])k++;
            else{
                //不匹配则在模式串中，继续向前搜索
                //比如pattern aabaac
                //待查找的串 aaeefaagef
                //     aeefaagef              aeefaagef
                //         aabaac                 aabaac
                //           |该位置不匹配 next[1]=0|
                while(k!=-1&&par[k+1]!=sour[j])k=next[k];
            }
            if(k==n2-1)return j-k;
        }

        return -1;
    }
}
