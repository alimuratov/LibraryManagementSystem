package main.book;

public class Review_stub {

    private String comments;
    private int rating;

    public Review_stub(String comment, int rate) {
        this.comments = comment;
        if (rate < 0 || rate > 8) {
            System.out.println("Rating should be 0 to 8");
            return;
        }
        else{
            this.rating = rate;
        }
    }

    public String getComments() {
        return comments;
    }

    public int getBookRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Book Review\n" + "Book Rating:" + rating + "\n" +  "Comment=" + comments;
    }
}
