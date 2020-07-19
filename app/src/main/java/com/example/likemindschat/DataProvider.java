package com.example.likemindschat;

public interface DataProvider<T> {

    T get(int position);

    int getCount();

    int getItemViewType(int position);

    int indexOf(T item);

    boolean contains(T item);

    void remove(int position);

    boolean isChecked(int position);
}