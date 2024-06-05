package crud;

public class DateCount {
    private String year;
    private String month;
    private int count;

    public DateCount(String year, String month, int count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}