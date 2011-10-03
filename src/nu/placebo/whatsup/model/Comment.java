package nu.placebo.whatsup.model;


import java.util.Date;

/**
 * Immutable class with information about one comment.
 */
public class Comment {

	private String author;
	
	private String commentText;
	
	private String title;
	
	private Date addedDate;
	
	public Comment(String author, String commentText, String title, Date addedDate) {
		this.author = author;
		this.commentText = commentText;
		this.title = title;
		this.addedDate = addedDate;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getCommentText() {
		return commentText;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getAddedDate() {
		return addedDate;
	}
}
