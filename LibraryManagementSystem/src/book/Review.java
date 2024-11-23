package book;

public class Review {

    private String comments;
    private int rating;

    public Review(String comment, int rate) {
        this.comments = comment;
        if (rate < 0 || rate > 8) {
            System.out.print("Rating should be 0 to 8");
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
        return "Book Review\nBook Rating: " + rating + "\n" +  "Comment: " + comments;
    }
}
