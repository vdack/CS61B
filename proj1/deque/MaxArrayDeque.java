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
        for (int i = 0; i < size(); i++){
            T e = get(i);
            if (comparator.compare(e,maxElement) > 0){
                maxElement = e;
            }
        }
        return maxElement;
    }

    public T max(Comparator<T> c){
        return preMax(c);
    }
}
