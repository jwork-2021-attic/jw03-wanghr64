package S191220107.sorter;

public class HeapSorter implements Sorter {

    private int[] a;

    @Override
    public void load(int[] a) {
        this.a = a;
    }

    private void swap(int i, int j) {
        int temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
    }

    private void adjustHeap(int i, int length) {
        int temp = a[i];
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
            if (k + 1 < length && a[k] < a[k + 1]) {
                k++;
            }
            if (a[k] > temp) {
                swap(i, k);
                i = k;
            } else {
                break;
            }
        }
        a[i] = temp;
    }

    private String plan = "";

    @Override
    public void sort() {
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            adjustHeap(i, a.length);
        }
        for (int j = a.length - 1; j > 0; j--) {
            swap(0, j);
            adjustHeap(0, j);
        }
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}
