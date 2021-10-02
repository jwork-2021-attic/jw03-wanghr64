package S191220107.sorter;

public class QuickSorter implements Sorter {

    private int[] a;

    private void quickSort(int left, int right) {
        if (left < right) {
            int partitionIndex = partition(left, right);
            quickSort(left, partitionIndex - 1);
            quickSort(partitionIndex + 1, right);
        }
    }

    private int partition(int left, int right) {
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (a[i] < a[pivot]) {
                swap(i, index);
                index++;
            }
        }
        swap(pivot, index - 1);
        return index - 1;
    }

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

    private String plan = "";

    @Override
    public void sort() {
        quickSort(0, a.length - 1);
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}
