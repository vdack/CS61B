package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{

    private Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c){
        super();
        this.comparator = c;
    }
    public T max(){
        return preMax(comparator);
    }

    private T preMax(Comparator<T> comparator) {
        T maxElement;
        if(this.size() == 0){
            return null;
        }
        else{
            maxElement = this.getFirst();
        }
        for (T e : this){
            if (comparator.compare(e,maxElement) == 1){
                maxElement = e;
            }
        }
        return maxElement;
    }

    public T max(Comparator<T> c){
        return preMax((Comparator<T>) c);
    }
}
