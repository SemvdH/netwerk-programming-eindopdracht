package netwerkprog.game.util.tree;

public class TreeNode<E>  {

    protected E element;
    protected TreeNode<E> left;
    protected TreeNode<E> right;

    public TreeNode(E e) {
        this.element = e;
    }

    public E getElement() {
        return element;
    }
}
