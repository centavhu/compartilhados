package crud;

public class CurrentYearUFCount {
    private String year;
    private String uf;
    private int count;

    public CurrentYearUFCount(String year, String uf, int count) {
        this.year = year;
        this.uf = uf;
        this.count = count;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUF() {
        return uf;
    }

    public void setUF(String uf) {
        this.uf = uf;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}