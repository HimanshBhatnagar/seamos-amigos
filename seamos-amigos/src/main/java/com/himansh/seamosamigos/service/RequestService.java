package com.himansh.seamosamigos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.himansh.seamosamigos.entity.Connections;
import com.himansh.seamosamigos.entity.FollowRequests;
import com.himansh.seamosamigos.entity.UserEntity;
import com.himansh.seamosamigos.repository.ConnectionRepository;
import com.himansh.seamosamigos.repository.RequestsRepository;
import com.himansh.seamosamigos.repository.UserRepository;

@Service
public class RequestService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConnectionRepository conRepository;
	@Autowired
	private RequestsRepository reqRepository;
	
	
	private UserEntity getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
	
	//Method for Follow Requests
	 public boolean acceptOrRejectRequest(int userId, int requestId,char response) throws Exception {
		 UserEntity ue=getUserById(userId);
		 FollowRequests request=null;
		 for(FollowRequests r: ue.getRequests()) {
			 if (r.getRequestId()==requestId) {
				request=r;
			}
		 }
		 if (request==null) {
			throw new Exception("No Request Found!!");
		 }
		switch (response) {
		case 'A':
			Connections con=new Connections();
			con.setUser1(request.getRequestedUser());
			con.setUser2(request.getRequestingUser());
			boolean result=conRepository.saveAndFlush(con)!=null;
			reqRepository.delete(request);
			return result;
		case 'R':
			reqRepository.delete(request);
			return true;
		default:
			return false;
		}	 
	 }
	 
	 public FollowRequests createRequest(int requestedUser,int requestingUser) throws Exception {
		 if (reqRepository.checkIfRequestAlreadyExists(requestedUser, requestingUser)!=null) {
				throw new Exception("Already Requested");
			}
		 if (conRepository.checkConnection(requestedUser, requestingUser)!=null) {
			throw new Exception("Already Following");
		}
		 if(requestedUser==requestingUser) {
			 throw new Exception("Can't follow yourself");
		 }
		 FollowRequests request=new FollowRequests();
		 request.setRequestedUser(getUserById(requestedUser));
		 request.setRequestingUser(getUserById(requestingUser));
		 return reqRepository.saveAndFlush(request);
	 }
	 
	@GetMapping(path="users/request")
	public List<FollowRequests> getAllRequests(int requestedUser){
		return reqRepository.getAllRequests(requestedUser);
	}
}
