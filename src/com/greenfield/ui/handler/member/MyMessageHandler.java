/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.ui.handler.member;

import com.greenfield.ui.action.member.MyMessageAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.MyMessageTypes;
import com.greenfield.common.dao.profile.MyMessageDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.member.MyMessageVO;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *
 * @author Qin
 */
public class MyMessageHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(MyMessageHandler.class); 
	
        public static final int PAGE_SIZE = 5;
        public static final int NUM_LINKS = 10;

	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
            MyMessageVO myMessageVO = (MyMessageVO) obj;
            String mode = myMessageVO.getMode();
            String type = myMessageVO.getType();
            MyMessageDAO messageDao = new MyMessageDAO();
            messageDao.setDatabase(database);

            System.out.println("MyMessageHandler");
            
            // mode - N, V, L, blank; type = blank, null, or N
            boolean loadList = false;
            if (mode != null && mode.equals(MyMessageAction.NEW_MODE)) {
                // display the list
                if (type != null && !type.equals(MyMessageAction.BLANK)) {
                    insertMyMessage(myMessageVO, messageDao);
                    myMessageVO.setCurrLink("1");  // for load list purpose
                    loadList = true;
                } else {
                    // do nothing, return the empty form for new message
                }
            } else if (mode != null && mode.equals(MyMessageAction.VIEW_MODE)) {
                // get the data
                loadMyMessage(myMessageVO, messageDao);

            } else if (mode != null && mode.equals(MyMessageAction.DELETE_MODE)) {
                // Do not do now - think about the "hole" issue after deletion
                //messageDao.deleteMyMessageFromDB(myMessageVO.getUserId(), 
                //        myMessageVO.getMessageType(), myMessageVO.getMessageSeq());
                loadList = true;
            } else if (mode != null && mode.equals(MyMessageAction.MODIFY_MODE)) {
                // two cases: first load on the page, or do update and load 
                if (type != null && !type.equals(MyMessageAction.BLANK)) {
                    // need to update, check message type and only do update if User send to itself
                    if (myMessageVO.getMessageType() != null && myMessageVO.getMessageType().equals(MyMessageTypes.US_TYPE)) {
                        messageDao.updateMyMessageToDB(myMessageVO);
                    }
                }
                
                // reload after update or for initial load
                loadMyMessage(myMessageVO, messageDao);
                
            } else {
                loadList = true;
            }
            
            if (loadList) {
                loadMessageList(myMessageVO, messageDao);
            }


            return myMessageVO;
	}
        
        
        private void loadMyMessage(MyMessageVO myMessageVO, MyMessageDAO messageDao) throws Exception {
            MyMessageVO vo = messageDao.getMyMessageFromDB(myMessageVO.getUserId(), 
                    myMessageVO.getMessageType(), myMessageVO.getMessageSeq());

            if (vo != null) {
                myMessageVO.setSubject(vo.getSubject());
                myMessageVO.setContent(vo.getContent());
                myMessageVO.setMDate(vo.getMDate());
                myMessageVO.setMessageSeq(vo.getMessageSeq());
                myMessageVO.setMessageType(vo.getMessageType());
            } else {
                // reset
                myMessageVO.setSubject("");
                myMessageVO.setContent("");
                myMessageVO.setMDate("");
            }
        }
        
        private void insertMyMessage(MyMessageVO vo, MyMessageDAO messageDao) {
            // need to find the max seq number
            try {
                String messageType = vo.getMessageType();
                if (messageType == null || messageType.equals("")) {
                    // if not defined, should be error
                    throw new Exception("message type not defined ... " + vo.getUserId());
                }
                int maxSeq = messageDao.getMaxSeqNumberFromDB(vo.getUserId(), vo.getMessageType());
                
                vo.setMessageSeq(maxSeq + 1);
                
                // use the current time to set mDate
                
                
                
                messageDao.insertMyMessageToDB(vo);
                
            } catch (Exception ex) {
                    // update error
                    ex.printStackTrace();
                    vo.setSuccess(false);
                    vo.setMessage("Error in insert the my message: " + ex.getMessage());
                    LOGGER.warn("Error in insert the my message: " + ex.getMessage());
            }
            
        }
        
        // messageSeq - passed from client to show which page of message to show
        // messageSeq: 1 -> max / PAGE_SIZE + 1;
        private void loadMessageList(MyMessageVO myMessageVO, MyMessageDAO messageDao) {
            try {
                String messageType = myMessageVO.getMessageType();
                if (messageType == null || messageType.equals("")) {
                    // if not defined, should be error
                    messageType = MyMessageTypes.US_TYPE;
                    myMessageVO.setMessageType(messageType);
                }
                
                // need to build the indexes
                int maxSeq = messageDao.getMaxSeqNumberFromDB(myMessageVO.getUserId(), myMessageVO.getMessageType());
                if (maxSeq == 0) {
                    // no record is found
                    myMessageVO.setMessageList(new Vector());
                    myMessageVO.setLinkList(new Vector());
                    myMessageVO.setFirstMessageSeq("");
                    return;
                }
                
                String currLink = myMessageVO.getCurrLink();
                if (currLink == null || currLink.equals("")) {
                    // if not defined, make it to 1
                    currLink = "1";
                }
                
                int currLinkNum = 0;
                try {
                    currLinkNum = (new Integer(currLink)).intValue();
                } catch (Exception ex) {
                    // ingore the error
                    currLinkNum = 1;
                }
                
                if (currLinkNum <= 0) {
                    currLinkNum = 1;   // begain from the first one
                }
                
                figureLinkList(myMessageVO, maxSeq, currLinkNum);
                
                int startSeq = (currLinkNum - 1) * PAGE_SIZE + 1;
                int endSeq   = currLinkNum * PAGE_SIZE;
                
                Vector messageList = messageDao.getMyMessageListFromDB(myMessageVO.getUserId(), myMessageVO.getMessageType(), startSeq, endSeq);
                myMessageVO.setMessageList(messageList);
                myMessageVO.setSuccess(true);
            } catch (Exception ex) {
                    // update error
                    ex.printStackTrace();
                    myMessageVO.setMessageList(new Vector());
                    myMessageVO.setLinkList(new Vector());
                    myMessageVO.setSuccess(false);
                    myMessageVO.setMessage("Error in select my message: " + ex.getMessage());
                    LOGGER.warn("Error in select my message: " + ex.getMessage());
            }
        }
        
        
        // according the max sequence, and the displaying sequence, to
        // build tghe list of the links to show; messageSeq start with 1 (and > 0)
        // 
        private void figureLinkList(MyMessageVO myMessageVO, int maxSeq, int currLink) {
            Vector list = new Vector();
            
            int totalPages = maxSeq / PAGE_SIZE;
            if (maxSeq % PAGE_SIZE != 0) {
                totalPages += 1;
            }
            
            // do some safe check there
            if (currLink <= 0 || currLink > totalPages) {
                currLink = 1;
            }
            
            int startLink = currLink - NUM_LINKS;
            int endLink = currLink + NUM_LINKS;
            
            if (startLink < 1) {
                startLink = 1;
            }
            
            if (endLink > totalPages) {
                endLink = totalPages;
            }
            
            // only build # links before and after the search link
            for (int i = startLink; i <= endLink; i ++) {
                list.add(new String(String.valueOf(i)));
            }
            
            int nextLink = currLink + 1;
            if (nextLink > totalPages) {
                nextLink = totalPages;
            }
            
            int prevLink = currLink - 1;
            if (prevLink < 1) {
                prevLink = 1;
            }
            
            int firstMessageSeq = (currLink - 1) * PAGE_SIZE + 1;
            
            myMessageVO.setLinkList(list);
            myMessageVO.setLastLink(new String(String.valueOf(totalPages)));
            myMessageVO.setPrevLink(new String(String.valueOf(prevLink)));
            myMessageVO.setNextLink(new String(String.valueOf(nextLink)));
            myMessageVO.setFirstMessageSeq(new String(String.valueOf(firstMessageSeq)));
            myMessageVO.setCurrLink(new String(String.valueOf(currLink)));
            
        }

}