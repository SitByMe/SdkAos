package sbmsdk.aos.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * desc   :
 * date   : 2024/9/23
 *
 * @author zoulinheng
 */
public class ListHelper<T> {
  @Nullable
  private RepeatChecker<T> repeatChecker = null;

  @NonNull
  private final OnDataChangedListener<T> onDataChangedListener;

  public ListHelper(@NonNull OnDataChangedListener<T> onDataChangedListener) {
    this.onDataChangedListener = onDataChangedListener;
  }

  public void setRepeatChecker(@Nullable RepeatChecker<T> checker) {
    this.repeatChecker = checker;
  }

  public boolean noRepeat() {
    return repeatChecker != null;
  }

  private final List<T> list = new ArrayList<>();

  @NonNull
  public List<T> get() {
    return new ArrayList<>(list);
  }

  @Nullable
  public T getItem(int position) {
    if (position >= list.size()) return null;
    return list.get(position);
  }

  public int size() {
    return list.size();
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public boolean contains(@Nullable T o) {
    return list.contains(o);
  }

  public void forEach(@NonNull Consumer<? super T> action) {
    list.forEach(action);
  }

  @NonNull
  public T[] toArray() {
    return (T[]) list.toArray();
  }

  public void add(T t) {
    if (isRepeat(t)) return;
    list.add(t);
  }

  public void add(int position, T t) {
    if (isRepeat(t)) return;
    list.add(position, t);
  }

  public void set(int position, T t) {
    if (isRepeat(t)) return;
    list.set(position, t);
  }

  public void remove(@Nullable T o) {
    list.remove(o);
  }

  public void remove(int position) {
    if (position >= list.size()) return;
    list.remove(position);

  }

  public boolean containsAll(@NonNull List<T> c) {
    return list.containsAll(c);
  }

  public void addAll(@NonNull List<T> c) {
    List<T> noRepeatList = getNoRepeatData(c);
    if (noRepeatList.isEmpty()) return;
    list.addAll(noRepeatList);
  }

  public void removeAll(@NonNull List<T> c) {
    list.removeAll(c);
  }

  public void removeIf(@NonNull Predicate<? super T> filter) {
    list.removeIf(filter);
  }

  public void retainAll(@NonNull List<T> c) {
    list.retainAll(c);
  }

  public void clear() {
    list.clear();
  }

  public void post() {
    onDataChangedListener.onChanged(get());
  }

  //筛选出不重复的数据
  private List<T> getNoRepeatData(List<T> from) {
    if (repeatChecker == null) return new ArrayList<>(from);
    List<T> rList = new ArrayList<>();
    for (T t : from) {
      if (!isRepeat(t)) {
        rList.add(t);
      }
    }
    return rList;
  }

  //是否是重复数据
  private boolean isRepeat(T t) {
    if (repeatChecker == null) return false;
    for (T t1 : list) {
      if (repeatChecker.check(t1, t)) {
        return true;
      }
    }
    return false;
  }

  public interface OnDataChangedListener<T> {
    void onChanged(@NonNull List<T> datas);
  }

  public interface RepeatChecker<T> {
    boolean check(T t1, T t2);
  }
}
