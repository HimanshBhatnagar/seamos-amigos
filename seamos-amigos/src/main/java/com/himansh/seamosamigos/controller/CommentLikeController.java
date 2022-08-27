package com.himansh.seamosamigos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.dto.CommentWebModel;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.CommentAndReplyService;
import com.himansh.seamosamigos.utility.CurrentUser;

@RestController
@RequestMapping("api/seamos-amigos/")
public class CommentLikeController {
	private final CommentAndReplyService commentService;
	private final Logger log = LoggerFactory.getLogger(CommentLikeController.class);
	private Integer userId = -1;

	public CommentLikeController(CommentAndReplyService commentService) {
		this.commentService = commentService;
	}
	
	@ModelAttribute
    public void fetchUser() {
    	userId  = CurrentUser.getCurrentUserId();
    }
	
	@GetMapping(path = "media/feed/comments")
	public List<CommentWebModel> getPhotoComments(@RequestParam(name = "photoId") Integer photoId) throws Exception{
		log.info("Request for: media/feed/comments");
		return commentService.getAllComments(photoId);		
	}
	
	@PostMapping(path = "media/feed/add-comment")
	public Boolean addComment(@RequestBody CommentWebModel commentWebModel) throws Exception{
		log.info("Request for: media/feed/add-comment");
		return commentService.saveComment(commentWebModel, userId) !=null;		
	}
	
	@PutMapping(path = "media/feed/update-comment")
	public Boolean updateComment(@RequestBody CommentWebModel commentWebModel) throws InAppException{
		log.info("Request for: media/feed/update-comment");
		return commentService.updateComment(commentWebModel, userId) !=null;		
	}
	
	@DeleteMapping(path = "media/feed/delete-comment")
	public String deleteComment(@RequestParam(name = "commentId") Integer commentId) throws Exception{
		log.info("Request for: media/feed/delete-comment");
		return commentService.deleteComment(commentId, userId)>0?"Deleted":"Comment Not Deleted";		
	}

}