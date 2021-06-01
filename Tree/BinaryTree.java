package main.learning2;


import java.util.*;

public class BinaryTree {
    public BinaryTree() {
        root=null;
    }

    private class node{
        private int value;
        private node left;
        private node right;
        node(){}
        node(int value){
            this.value=value;
            this.left=null;
            this.right=null;
        }
    }
    private node root;
    private int size;

    /*递归实现
    public ArrayList preorder(node root){
        ArrayList<Integer> res=new ArrayList<>(10);
        if(root!=null){
            //System.out.println("Pre-Oder:");
            res.add(root.value);
            preorder(root.left);
            preorder(root.right);
        }
        return res;
    }
    */
    public ArrayList preorder(){
        node current=root;
        ArrayList<Integer>res=new ArrayList<>(10);
        Stack<node>stk=new Stack<>();
        while(!stk.isEmpty()||current!=null){
            while(current!=null){
                res.add(current.value);
                stk.push(current);
                current=current.left;
            }
            if(!stk.isEmpty()){
                current=stk.pop();
                current=current.right;
            }

        }
        return res;
    }


    public ArrayList inorder(){
        node current=root;
        ArrayList<Integer>res=new ArrayList<>(10);
        Stack<node>stk=new Stack<>();
        while(!stk.isEmpty()||current!=null){
            while(current!=null){
                stk.push(current);
                current=current.left;
            }
            if(!stk.isEmpty()){
                current=stk.pop();
                res.add(current.value);
                current=current.right;
            }

        }
        return res;
    }


    public ArrayList posorder(){
        node current=root;
        ArrayList<Integer>res=new ArrayList<>(10);
        Stack<node>stk=new Stack<>();
        while(!stk.isEmpty()||current!=null){
            while(current!=null){
                res.add(current.value);
                stk.push(current);
                current=current.right;
            }
            if(!stk.isEmpty()){
                current=stk.pop();
                current=current.left;
            }

        }
        Collections.reverse(res);//reverse
        return res;
    }

    public ArrayList levelorder(){
        node current=root;
        ArrayList<Integer>res=new ArrayList<>(10);
        Deque<node> que=new ArrayDeque<>();
        if(current==null)return res;
        que.add(current);
        while(!que.isEmpty()){
            int s= que.size();
            for(int i=0;i<s;i++){
                node temp= que.pollFirst();
                if(temp.left!=null)que.add(temp.left);
                if(temp.right!=null)que.add(temp.right);
                res.add(temp.value);
            }
        }
        return res;
    }
    public boolean insert(int element){
        node temp=new node(element);
        if(root==null){
            root=temp;
            size++;
            return true;
        }
        node current=root;
        node parent=root;
        while(current!=null){
            parent=current;
            if(current.value<element){
                current=current.right;
                if(current==null){
                    parent.right=temp;
                    size++;
                    return true;
                }
            }
            else if(current.value>element){
                current=current.left;
                if(current==null){
                    parent.left=temp;
                    size++;
                    return true;
                }

            }
            else
                return false;
        }
        return false;
    }

    public node find(int element){
        if(root==null)return null;
        node current=root;
        while(true){
            if(element<current.value)
                current=current.left;
            else if(element==current.value)
                return current;
            else
                current=current.right;
            if(current==null)return null;
        }

    }

    public boolean delete(int Key) {
        if (root == null) return false;

        node current = root;
        node parent = root;
        boolean isleft = true;
        while (current != null && current.value != Key) {
            parent = current;
            if (current.value < Key) {
                current = current.right;
                isleft = false;
            } else if (current.value > Key) {
                current = current.left;
                isleft = true;
            }
        }
        if (current == null) return false;

        if (current.left == null && current.right == null) {
            if (current == root) {
                root = null;
            } else if (isleft) {
                parent.left = null;
            } else
                parent.right = null;
            return true;
        } else if (current.left == null) {
            if (current == root)
                root = current.right;
            else if(isleft)
                parent.left=current.right;
            else
                parent.right=current.right;
        }
        else if(current.right==null){
            if(current==root)
                root=current.left;
            else if(isleft)
                parent.left=current.left;
            else
                parent.right=current.left;
            }
        else {
            node later=min(current);
            if(current==root){
                root=later;
            }
            else if(isleft)
                parent.left=later;
            else
                parent.right=later;
            later.left=current.left;
            return true;
        }
        return false;
    }

    private node min(node del){
        node current=del;
        node parent=del;
        node suss=del.right;
        while(current!=null){
            parent=suss;
            suss=current;
            current=current.left;
        }
        if(suss!=del.right){
            parent.left=suss.right;
            suss.right=del.right;
        }
        return suss;
    }
    private node  findSuccessor(node delNode){
        node parent = delNode;
        node successor = delNode;
        node current = delNode.right;
        while(current != null){
            parent = successor;
            successor = current;
            current = current.left;
        }

        if(successor != delNode.right){
            parent.left = successor.right;
            successor.right = delNode.right;
        }
        return successor;
    }
    private int height(node root){
        if(root==null)
            return 0;
        else{
            int i=height(root.left);
            int j=height(root.right);
            return i<j? j+1:i+1;
        }
    }

    private int height2(node root){
        if(root==null)return 0;
        Deque<node> que=new ArrayDeque<>();
        que.add(root);
        int h=0;
        while(!que.isEmpty()){
            int s=que.size();
            for(int i=0;i<s;i++){
                node temp=que.pollFirst();
                if(temp.left!=null)que.add(temp.left);
                if(temp.right!=null)que.add(temp.right);
            }
            h++;
        }
        return h;
    }
    public int size(){
        return size;
    }
    public int height(){
        return height2(root);
        //return height(root);
    }
    public static void main(String[] args) {
        BinaryTree trees=new BinaryTree();
        trees.insert(2);
        trees.insert(1);
        trees.insert(4);
        trees.insert(3);
        trees.insert(7);
        trees.insert(5);
        trees.insert(8);
        trees.insert(6);
        System.out.println(trees.preorder());
        System.out.println(trees.inorder());
        System.out.println(trees.posorder());
        System.out.println(trees.levelorder());
        System.out.println("size: "+trees.size());
        trees.delete(7);
        System.out.println(trees.inorder());
        System.out.println(trees.find(9));

    }
}
