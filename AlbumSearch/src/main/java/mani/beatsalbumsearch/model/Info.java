package mani.beatsalbumsearch.model;

/**
 * Contains paging information used to display the results
 */
public class Info {
    int offset;
    int count;
    int total;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
