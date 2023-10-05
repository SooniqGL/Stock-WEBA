/*
 * Created on Jun 20, 2006
 */
package com.greenfield.ui.action.member;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.member.MyMessageHandler;
import com.greenfield.common.object.member.MyMessageVO;


/**
 * @author zhangqx
 */
public class MyMessageAction extends BaseAction {
	
	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String NEW_MODE = "N";
        public static final String VIEW_MODE = "V";
        public static final String LIST_MODE = "L";
        public static final String DELETE_MODE = "D";
        public static final String MODIFY_MODE = "M";
	
	/** return string */
	public static final String NEWMESSAGE = "newmessage";
        public static final String VIEWMESSAGE = "viewmessage";
        public static final String MYMESSAGELIST = "mymessagelist";
	
        private MyMessageVO inputVO = new MyMessageVO();
	
        public String executeAction()
                throws Exception {

                String retString = MYMESSAGELIST;
                String mode = inputVO.getMode();
                String type = inputVO.getType();
                
                // set default
                if (type == null || type.equals("")) {
                    type = BLANK;
                    inputVO.setType(type);
                }

                System.out.println("mode: " + mode);
                System.out.println("type: " + inputVO.getType());

                try {
                        // define the return string based on the mode
                        if (mode == null || mode.equals("")) {
                            inputVO.setMode(BLANK);
                            retString = MYMESSAGELIST;
                        } else if (mode.equals(NEW_MODE)) {
                            if (type.equals(BLANK)) {
                                // blank form for user to create one message
                                retString = NEWMESSAGE;
                            } else {
                                // type = N, message will be saved
                                retString = MYMESSAGELIST;
                            }
                        } else if (mode.equals(VIEW_MODE)) {
                            retString = VIEWMESSAGE;
                        } else if (mode.equals(DELETE_MODE)) {
                            retString = MYMESSAGELIST;
                        } else if (mode.equals(MODIFY_MODE)) {
                            retString = VIEWMESSAGE;
                        }

                        // pass User ID
                        inputVO.setUserId(user.getUserId());


                        MyMessageHandler handler = new MyMessageHandler();
                        handler.execute(user, inputVO, sessionContext);	

                        
			
                } catch (Exception e) {

                        //c_logger.instr(ke);	
                        e.printStackTrace();
                        inputVO.setMessage("Some system error: " + e.getMessage());
                        inputVO.setSuccess(false);
                        retString = MYMESSAGELIST;
                }

                System.out.println("return: " + retString);
                
                return retString;  
        }

		public MyMessageVO getInputVO() {
			return inputVO;
		}

		public void setInputVO(MyMessageVO inputVO) {
			this.inputVO = inputVO;
		}


}