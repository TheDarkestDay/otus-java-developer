package com.abrenchev;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class DIYListIterator<E> implements ListIterator<E> {
    private DIYList<E> list;

    private int cursor = 0;

    public DIYListIterator(DIYList<E> list) {
        this.list = list;
    }

    public boolean hasNext() {
        throw new UnsupportedOperationException();
    }

    public E next() {
        E result = list.get(cursor);
        cursor++;

        return result;
    }

    public boolean hasPrevious() {
        throw new UnsupportedOperationException();
    }

    public E previous() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int previousIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(E e) {
        list.set(cursor - 1, e);
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }
}

public class DIYList<T> implements List<T> {
    private final int BUFFER_SIZE = 10;

    private Object[] array;

    private int lastElementIndex = -1;

    private int size = 0;

    public static void main(String... args) {
        List<Integer> list = new DIYList<Integer>(20);

        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

        if (list.size() != 20) {
            throw new RuntimeException("List size is wrong");
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != i + 1) {
                throw new RuntimeException("addAll messed with the list content");
            }
        }

        int testArrSize = 20;
        List<Integer> testArr = new ArrayList<>();
        for (int i = 0; i < testArrSize; i++) {
            testArr.add(ThreadLocalRandom.current().nextInt());
        }

        Collections.copy(list, testArr);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(testArr.get(i))) {
                throw new RuntimeException("Collections.copy messed with the content");
            }
        }

        Collections.sort(testArr);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(testArr.get(i))) {
                throw new RuntimeException("Collections.sort works incorrectly");
            }
        }
    }

    public DIYList(int size) {
        array = new Object[size];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean contains(Object obj) {
        for (Object element : array) {
            if (obj.equals(element)) {
                return true;
            }
        }

        return false;
    }

    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public T[] toArray() {
        return (T[]) array;
    }

    public <E> E[] toArray(E[] givenArray) {
        throw new UnsupportedOperationException();
    }

    public boolean add(T elem) {
        lastElementIndex++;
        if (lastElementIndex >= array.length) {
            array = Arrays.copyOf(array, array.length + BUFFER_SIZE);
        }

        array[lastElementIndex] = elem;
        size++;

        return true;
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public T get(int index) {
        return (T) array[index];
    }

    public T set(int index, T element) {
        array[index] = element;

        return element;
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(obj)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(Object obj) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<T> listIterator() {
        return new DIYListIterator<>(this);
    }

    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    public List<T> subList(int start, int end) {
        throw new UnsupportedOperationException();
    }
}