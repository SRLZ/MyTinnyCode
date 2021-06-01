import java.util.Arrays;
public class Heap {
    public static void main(String[] args) {
        //it is the case test.
        Heap heap=new Heap(new int[]{99,-6,3,4,5,6,7,88,9,10,18,21,0,-1,66,8997,7,5,2,1});
        heap.display();
        System.out.println("========");
        heap.offer(-99);
        heap.offer(0);
        heap.offer(9000);
        heap.offer(56);
        heap.offer(88);
        for (int i = 0; i < 25; i++) {
            System.out.println(heap.poll());
        }
    }
    private int[] element;
    int size=0;
     public Heap(int[]arr){
        int s=arr.length;
        size=s;
        element=new int[s*2];
        for(int i=0;i<s;i++)element[i]=arr[i];
        heapify();
     }
    private void siftDown(int k,int key,int n){
        int t= n >>>1;//非叶子节点的边界
        while(k<t){
            //System.out.println("down");
            int child=(k<<1)+1;
            int ele=element[child];
            if(child+1<n&&element[child+1]<ele){
                child=child+1;
                ele=element[child];
            }
            if(key<=ele)break;
            element[k]=ele;
            k=child;
        }
        element[k]=key;
    }
    private void siftUp(int k,int key){
        while(k>0){
            int parent=(k-1)>>1;
            int e=element[parent];
            if(key>=element[parent])break;
            element[k]=e;
            k=parent;
        }
        element[k]=key;
    }
    public boolean offer(int ele){
        int index=size;
        if(index>=element.length){
            element= Arrays.copyOf(element,size*2);
        }
        siftUp(index,ele);
        size++;
        return true;
    }
    public int poll(){

        if (size==0)throw new NullPointerException("空");
        int res=element[0];
        int n=size-1;
        int key=element[n];
        element[n]=0;
        if(n>0)siftDown(0,key,n);
        size--;
        return res;
    }
    public void display(){
         for(int i=0;i<size;i++) System.out.println(element[i]);
    }
    private void heapify(){
         int t=(size>>1)-1;
         for(int i=t;i>=0;i--){
             siftDown(i,element[i],size);
         }
    }
}
