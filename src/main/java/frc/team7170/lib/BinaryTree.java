package frc.team7170.lib;

import java.util.*;

// TODO: delete this once we're sure we dont need it anymore...
public class BinaryTree<T extends Comparable<T>> extends AbstractCollection<T> {

    private final T value;
    private BinaryTree<T> left;
    private BinaryTree<T> right;

    public BinaryTree(T value) {
        if (value == null) {
            throw new NullPointerException("binary trees cannot contain null elements");
        }
        this.value = value;
    }

    public T getValue() {
        return value;
    }



    @Override
    public int size() {
        return 1 + (left != null ? left.size() : 0) + (right != null ? right.size() : 0);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // TODO: use search
        return false;
    }

    private class BinaryTreeIterator implements Iterator<T> {

        private Deque<BinaryTree<T>> traverseHistory = new ArrayDeque<>();

        private BinaryTreeIterator() {
            populateHistoryWithLeftLineage(BinaryTree.this);
        }

        @Override
        public boolean hasNext() {
            return !traverseHistory.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T ret = traverseHistory.getLast().value;
            traverseToNext();
            return ret;
        }

        private void traverseToNext() {
            BinaryTree<T> last = traverseHistory.peekLast();
            // last should never be null here.
            if (last.right != null) {
                populateHistoryWithLeftLineage(last.right);
                return;
            }
            BinaryTree<T> removed;
            do {
                removed = traverseHistory.removeLast();
                last = traverseHistory.peekLast();
            } while (last != null && removed == last.right);
        }

        private void populateHistoryWithLeftLineage(BinaryTree<T> binaryTree) {
            while (binaryTree != null) {
                traverseHistory.add(binaryTree);
                binaryTree = binaryTree.left;
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public Object[] toArray() {
        int size = size();
        Object[] array = new Object[size];
        Iterator<T> iterator = iterator();
        for (int i = 0; i < size; ++i) {
            try {
                array[i] = iterator.next();
            } catch (NoSuchElementException e) {
                throw new AssertionError(e);
            }
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R[] toArray(R[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        try {
            if (a.length >= size()) {
                Iterator<T> iterator = iterator();
                for (int i = 0; i < a.length; ++i) {
                    if (iterator.hasNext()) {
                        a[i] = (R) iterator.next();
                    } else {
                        a[i] = null;
                        break;
                    }
                }
                return a;
            } else {
                return (R[]) toArray();
            }
        } catch (ClassCastException e) {
            throw new ArrayStoreException();
        }
    }

    @Override
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException("binary trees cannot contain null elements");
        }
        if (t.compareTo(value) < 0) {
            if (left == null) {
                left = new BinaryTree<>(t);
            } else {
                left.add(t);
            }
        } else if (t.compareTo(value) > 0) {
            if (right == null) {
                right = new BinaryTree<>(t);
            } else {
                right.add(t);
            }
        } else {  // t.compareTo(value) == 0 || t.equals(value)
            return false;  // binary trees cannot contain duplicates!
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
